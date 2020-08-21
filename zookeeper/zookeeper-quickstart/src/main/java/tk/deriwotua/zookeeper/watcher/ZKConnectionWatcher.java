package tk.deriwotua.zookeeper.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 监视器首先实现Watcher接口process()方法
 */
public class ZKConnectionWatcher implements Watcher {

    // 计数器对象
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    // 连接对象
    static ZooKeeper zooKeeper;

    /**
     * 当服务端有消息通知客户端时 回调process方法
     * @param event
     */
    public void process(WatchedEvent event) {
        try {
            // 事件类型为 none
            if (event.getType() == Event.EventType.None) {
                /**
                 * KeeperState 通知四种状态
                 *  `SyncConnected` 客户端与服务器正常连接时
                 *  `Disconnected` 客户端与服务器断开连接时
                 *  `Expired` 会话session失效时
                 *  `AuthFailed` 身份认证失败时
                 */
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    // 连接创建成功后唤醒主线程
                    countDownLatch.countDown();
                } else if (event.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("断开连接！");
                    /**
                     * 异常断开后比如网络中断这里可以捕获到连接断开事件
                     * 在这里可以编写一些邮件通知逻辑
                     */
                } else if (event.getState() == Event.KeeperState.Expired) {
                    // 连接中断后如果在超时时间内没有重连上服务端就被这里捕获到
                    System.out.println("会话超时!");
                    // 超时后会自动移除连接对象这里捕获到超时事件后再次创建新客户端到服务器连接对象
                    zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZKConnectionWatcher());
                } else if (event.getState() == Event.KeeperState.AuthFailed) {
                    /**
                     * 捕获认证失败事件
                     */
                    System.out.println("认证失败！");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            /**
             * 自定义watcher创建zookeeper连接(异步)
             * 如果连接中断在超时时间内zookeeper不会立即释放连接而是会保留zooKeeper连接对象且在此期间会尝试重连超过超时时间后会移除掉
             */
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZKConnectionWatcher());
            // 阻塞线程等待连接的创建
            countDownLatch.await();
            // 连接创建成功后通过countDownLatch.countDown()唤醒主线程 获取会话id
            System.out.println(zooKeeper.getSessionId());

            /**
             * 命令行创建 node1节点 设置权限
             *  create /node1 "node1"
             *  addauth digest deriwotua:123456
             *  setAcl /node1 auth:deriwotua:cdrwa
             * 然后读取node1节点数据
             */
            // 添加授权用户
            zooKeeper.addAuthInfo("digest", "deriwotua:123456".getBytes());
            // 读取node1节点数据
            byte[] bs = zooKeeper.getData("/node1", false, null);
            System.out.println(new String(bs));  // node1
            // 如果这里授权信息错误 这里密码错误
            zooKeeper.addAuthInfo("digest", "deriwotua:1234561".getBytes());
            // 读取node1节点数据 会触发认证失败异常 上面监听器会捕获到认证失败的
            bs = zooKeeper.getData("/node1", false, null);

            Thread.sleep(50000);
            zooKeeper.close();
            System.out.println("结束");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
