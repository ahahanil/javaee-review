package tk.deriwotua.zookeeper.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *  通过exists方法给节点注册可以捕获到节点的三种事件类型
 *      NodeCreated:节点创建
 *      NodeDeleted:节点删除
 *      NodeDataChanged:节点内容发生变化
 */
public class ZKWatcherExists {

    String IP = "127.0.0.1:2181";
    ZooKeeper zooKeeper = null;

    @Before
    public void before() throws IOException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // 连接zookeeper客户端
        zooKeeper = new ZooKeeper(IP, 6000,
                /**
                 * Watcher监听zookeeper事件变化
                 */
                new Watcher() {
                    public void process(WatchedEvent event) {
                        System.out.println("连接对象的参数!");
                        // 连接成功
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            countDownLatch.countDown();
                        }
                        System.out.println("path=" + event.getPath());
                        System.out.println("eventType=" + event.getType());
                    }
                });
        countDownLatch.await();
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }

    /**
     * 方式一：通过exists方法给节点注册使用连接对象的监视器
     * 方法执行后使用命令行执行
     *      create /watcher1 "watcher1"
     *  就会触发上面Watcher#process()可以捕获到节点NodeCreated创建事件
     * 命令行执行
     *      set /watcher1 "watcher2"
     *  同样会触发上面Watcher#process()可以捕获到节点NodeDataChanged内容变更事件
     * 命令行执行
     *      delete /watcher1
     *  同样会触发上面Watcher#process()可以捕获到节点NodeDeleted节点删除事件
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherExists1() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:使用上面创建连接对象时参数指定匿名内部类实现watcher
         */
        zooKeeper.exists("/watcher1", true);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 方式二：通过自定义监听器
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherExists2() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:自定义watcher对象
         */
        zooKeeper.exists("/watcher1", new Watcher() {
            /**
             * 自定义Watcher监听器
             * @param event
             */
            public void process(WatchedEvent event) {
                System.out.println("自定义watcher");
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
            }
        });
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * Watcher事件是一次性的
     *  命令行创建 watcher1节点
     *      create /watcher1 "watcher1"
     *  然后执行本方法给 watcher1注册自定义监听器
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherExists3() throws KeeperException, InterruptedException {
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    /**
                     *  命令行修改 watcher1节点
                     *      set /watcher1 "watcher11"
                     *  此时在程序控制台里可以看到 下面打印的信息
                     */
                    System.out.println("自定义watcher");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    /**
                     * 然后再次命令行修改 watcher1节点
                     *      set /watcher1 "watcher111"
                     * 此时在程序控制台里就不会再有上面信息打印 因为watcher是一次性的
                     *      一次注册一次通知
                     * 如果需要在 watcher1节点任何变更都能收到通知怎么办
                     *      可以在捕获到后再次给 watcher1节点注册监听器
                     */
                    zooKeeper.exists("/watcher1", this);
                    /**
                     * 此后Watcher就可以一直监听watcher1节点数据变化
                     */
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        zooKeeper.exists("/watcher1", watcher);
        Thread.sleep(80000);
        System.out.println("结束");
    }

    /**
     * 可以给节点注册多个监听器对象
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherExists4() throws KeeperException, InterruptedException {
        /**
         * 监听器一
         */
        zooKeeper.exists("/watcher1", new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("1");
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
            }
        });
        /**
         * 监听器二
         */
        zooKeeper.exists("/watcher1", new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("2");
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
            }
        });
        Thread.sleep(80000);
        System.out.println("结束");
    }
}
