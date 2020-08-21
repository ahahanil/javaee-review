package tk.deriwotua.zookeeper.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 *  获取zookeeper连接对象
 *  创建Locks锁节点
 *      创建临时有序子节点
 *  尝试获取锁
 *      获取Locks节点下的所有子节点
 *      对List子节点进行排序
 *      上面创建的临时有序子节点在List中的位置位于0时表示获取到锁
 *      否则监听上一个位置子节点是否删除(释放锁)
 *          删除的话再次尝试获取锁
 *          未被删除表明上一个节点处于等待锁或获得锁后还未释放锁时此时通过监视器wait方法等待获取锁
 *              等到监视器捕获到节点删除事件(释放锁)时唤醒监视器等待的线程去尝试获取锁
 *  释放锁
 *      删除临时有序节点
 *      关闭本次客户端会话
 */
public class MyLock {
    //  zk的连接串
    String IP = "127.0.0.1:2181";
    //  计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);
    //ZooKeeper配置信息
    ZooKeeper zooKeeper;
    private static final String LOCK_ROOT_PATH = "/Locks";
    private static final String LOCK_NODE_NAME = "Lock_";
    /**
     * 存储临时有序节点生成后节点path
     */
    private String lockPath;

    // 打开zookeeper连接
    public MyLock() {
        try {
            zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.None) {
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            System.out.println("连接成功!");
                            // 唤醒线程继续向下执行
                            countDownLatch.countDown();
                        }
                    }
                }
            });
            countDownLatch.await();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //获取锁
    public void acquireLock() throws Exception {
        //创建锁节点
        createLock();
        //尝试获取锁
        attemptLock();
    }

    //创建锁节点
    private void createLock() throws Exception {
        //判断Locks是否存在，不存在创建
        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false);
        if (stat == null) {
            // 不存在时创建节点
            zooKeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 创建临时有序子节点
        lockPath = zooKeeper.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("节点创建成功:" + lockPath);
    }

    /**
     * 监视器对象，监视上一个节点是否被删除
     *  删除即上一个获取锁后执行完锁操作后释放锁
     */
    Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                // 捕获到节点删除事件后 同步代码块唤醒等待线程
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    };

    /**
     * 尝试获取锁
     * @throws Exception
     */
    private void attemptLock() throws Exception {
        // 获取Locks节点下的所有子节点
        List<String> list = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
        // 对子节点进行排序 自然顺序
        Collections.sort(list);
        // 临时有序节点path: /Locks/Lock_000000001 取出子节点名称 Lock_000000001
        int index = list.indexOf(lockPath.substring(LOCK_ROOT_PATH.length() + 1));
        if (index == 0) {
            System.out.println("获取锁成功!");
            return;
        } else {
            // 上一个节点的路径
            String path = list.get(index - 1);
            /**
             * 监视上一个节点
             */
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + path, watcher);
            // 上一个节点已经删除锁释放掉了
            if (stat == null) {
                // 再次尝试获取锁
                attemptLock();
            } else {
                // 上一个节点处于等待锁或获得锁后还未释放锁时等待
                synchronized (watcher) {
                    // 等待获取锁 当监听器捕获到节点删除时唤醒这里 继续执行
                    watcher.wait();
                }
                attemptLock();
            }
        }

    }

    /**
     * 释放锁
     * @throws Exception
     */
    public void releaseLock() throws Exception {
        //删除临时有序节点
        zooKeeper.delete(this.lockPath, -1);
        zooKeeper.close();
        System.out.println("锁已经释放:" + this.lockPath);
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        try {
            MyLock myLock = new MyLock();
            myLock.createLock();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}