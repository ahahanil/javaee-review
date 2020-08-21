package tk.deriwotua.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * zookeeper客户端连接服务器
 */
public class ZookeeperConnection {
    public static void main(String[] args) {
        try {
            // 计数器对象
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            /**
             * ZooKeeper是异步的下面创建实例化对象不代表与服务器端连接成功
             * 所以上面定义了CountDownLatch计数器对象 然后通过await主线程阻塞等待连接对象的创建成功
             *  arg1:服务器的ip和端口
             *  arg2:客户端与服务器之间的会话超时时间  以毫秒为单位的
             *  arg3:监视器对象 当与服务器端连接建立成功后服务器会推送信息过来执行回调process方法
             */
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Watcher() {
                public void process(WatchedEvent event) {
                    // 连接成功状态码
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("连接创建成功!");
                        // 连接成功后唤醒主线程
                        countDownLatch.countDown();
                    }
                }
            });
            // ZooKeeper是异步的下面创建实例化对象不代表与服务器端连接成功
            // 主线程阻塞等待连接对象的创建成功
            countDownLatch.await();

            //连接成功后会唤醒主线程
            // 会话编号
            System.out.println(zooKeeper.getSessionId());
            zooKeeper.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
