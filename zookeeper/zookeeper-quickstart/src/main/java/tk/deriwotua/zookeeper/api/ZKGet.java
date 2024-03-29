package tk.deriwotua.zookeeper.api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKGet {

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
     * 获取节点数据
     *
     * @throws Exception
     */
    @Test
    public void get1() throws Exception {
        Stat stat = new Stat();
        /**
         * arg1:节点的路径
         * arg3:读取节点属性的对象
         */
        byte[] bys = zooKeeper.getData("/get/node1", false, stat);
        // 打印数据
        System.out.println(new String(bys));
        // 版本信息
        System.out.println(stat.getVersion());
    }

    /**
     * 异步方式获取节点数据
     *
     * @throws Exception
     */
    @Test
    public void get2() throws Exception {
        zooKeeper.getData("/get/node1", false, new AsyncCallback.DataCallback() {
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                // 0代表读取成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 上下文参数对象
                System.out.println(ctx);
                // 数据
                System.out.println(new String(data));
                // 属性对象
                System.out.println(stat.getVersion());
            }
        }, "I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}