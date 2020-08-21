package tk.deriwotua.zookeeper.api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKDelete {
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
     * 删除节点
     *
     * @throws Exception
     */
    @Test
    public void delete1() throws Exception {
        /**
         * arg1:删除节点的节点路径
         * arg2:数据版本信息 -1代表删除节点时不考虑版本信息
         */
        zooKeeper.delete("/delete/node1", -1);
    }

    /**
     * 删除节点限定版本号
     *
     * @throws Exception
     */
    @Test
    public void delete2() throws Exception {
        /**
         * arg1:删除节点的节点路径
         * arg2:数据版本信息 版本号不一致会报 BadVersionException异常
         */
        zooKeeper.delete("/delete/node1", 2);
    }

    /**
     * 异步方式删除节点
     *
     * @throws Exception
     */
    @Test
    public void delete3() throws Exception {
        /**
         * arg1:删除节点的节点路径
         * arg2:数据版本信息 版本号不一致会报 BadVersionException异常
         * arg3:异步接收回调方法
         * arg4:异步回调上下文参数
         */
        zooKeeper.delete("/delete/node2", -1,
                new AsyncCallback.VoidCallback() {
                    public void processResult(int rc, String path, Object ctx) {
                        // 0代表删除成功
                        System.out.println(rc);
                        // 节点的路径
                        System.out.println(path);
                        // 上下文参数对象
                        System.out.println(ctx);
                    }
                }, "I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}