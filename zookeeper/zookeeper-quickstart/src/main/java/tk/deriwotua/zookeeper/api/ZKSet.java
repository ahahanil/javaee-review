package tk.deriwotua.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 更新节点
 */
public class ZKSet {

    String IP = "127.0.0.1:2181";
    ZooKeeper zooKeeper;

    @Before
    public void before() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        /**
         * arg1:zookeeper服务器的ip地址和端口号
         * arg2:连接的超时时间  以毫秒为单位
         * arg3:监听器对象
         */
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                }
            }
        });
        // 使主线程阻塞等待
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        zooKeeper.close();
    }

    /**
     * 更新节点
     *
     * @throws Exception
     */
    @Test
    public void set1() throws Exception {
        /**
         * 命令行
         *  create /set/node1 "node1"
         *  set /set/node1 "node13"
         * api
         *  arg1:节点的路径
         *  arg2:修改的数据
         *  arg3:数据版本号 -1代表版本号不参与更新 （更新时不限定当前版本号乐观锁)
         * 更新后会返回节点stat属性描述对象
         */
        Stat stat = zooKeeper.setData("/set/node1", "node13".getBytes(), -1);
        // 当前节点的版本号
        System.out.println(stat.getVersion());

    }

    /**
     * 更新节点时限定版本号
     *
     * @throws Exception
     */
    @Test
    public void set2() throws Exception {
        /**
         * 命令行
         *  create /set/node1 "node1"
         *  set /set/node1 "node14" 2
         * api
         *  arg1:节点的路径
         *  arg2:修改的数据
         *  arg3:数据版本号 限定版本号时版本号不一致会报 BadVersionException异常
         * 更新后会返回节点stat属性描述对象
         */
        Stat stat = zooKeeper.setData("/set/node1", "node14".getBytes(), 2);
        // 当前节点的版本号
        System.out.println(stat.getVersion());

    }

    /**
     * 异步方式更新
     *
     * @throws Exception
     */
    @Test
    public void set3() throws Exception {
        /**
         *  arg1:节点的路径
         *  arg2:修改的数据
         *  arg3:数据版本号
         *  arg4:回调方法
         *  arg5:上下文信息
         */
        zooKeeper.setData("/set/node1", "node14".getBytes(), -1,
                new AsyncCallback.StatCallback() {
                    /**
                     * 回调方法
                     * @param rc 0代表修改成功
                     * @param path 节点的路径
                     * @param ctx 上下文参数对象
                     * @param stat 属性描述对象
                     */
                    public void processResult(int rc, String path, Object ctx, Stat stat) {
                        // 0代表修改成功
                        System.out.println(rc);
                        // 节点的路径
                        System.out.println(path);
                        // 上下文参数对象
                        System.out.println(ctx);
                        // 属性描述对象
                        System.out.println(stat.getVersion());
                    }
                }, "I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
