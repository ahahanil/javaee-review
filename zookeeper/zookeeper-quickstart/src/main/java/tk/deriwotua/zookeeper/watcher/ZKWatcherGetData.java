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
 * getData()方法可以捕获到节点的两种事件
 * NodeDeleted:节点删除
 * NodeDataChanged:节点内容发生变化
 */
public class ZKWatcherGetData {

    String IP = "127.0.0.1:2181";
    ZooKeeper zooKeeper = null;

    @Before
    public void before() throws IOException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // 连接zookeeper客户端
        zooKeeper = new ZooKeeper(IP, 6000,
                /**
                 * 创建连接时匿名内部类监听器对象
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
     * 通过getData方法给节点注册连接创建时匿名内部类监听器对象(非一次性的)
     *
     * 命令行先创建 watcher2节点
     *      create /watcher2 "watcher2"
     * 然后执行此方法
     * 再命令行中修改 watcher2节点
     *      set /watcher2 "watcher22"
     * 上面匿名内部类监听器对象可捕获到此次watcher2数据变更
     * 再命令行删除 watcher2节点
     *      delete /watcher2
     * 上面匿名内部类监听器对象可捕获到此次watcher2节点删除
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetData1() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:使用连接对象中的watcher
         */
        zooKeeper.getData("/watcher2", true, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 通过getData()方法给节点注册自定义匿名内部类监听器(非一次性的)
     *      可以一直捕获到节点数据变更、节点删除
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetData2() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:自定义watcher对象
         */
        zooKeeper.getData("/watcher2", new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("自定义watcher");
                System.out.println("path=" + event.getPath());
                System.out.println("eventType=" + event.getType());
            }
        }, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 注册Watcher实现类方式注册监听器(一次性的)
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetData3() throws KeeperException, InterruptedException {
        // 一次性
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("自定义watcher");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    /**
                     * 注册需要注意节点数据变更时再次注册
                     * 节点被删除了还注册会导致节点不存在会报错
                     */
                    if (event.getType() == Event.EventType.NodeDataChanged) {
                        /**
                         * 每次捕获后重新注册保证监听器一直可用
                         */
                        zooKeeper.getData("/watcher2", this, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        zooKeeper.getData("/watcher2", watcher, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 给节点注册多个监听器
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetData4() throws KeeperException, InterruptedException {
        // 注册多个监听器对象
        zooKeeper.getData("/watcher2", new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("1");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeDataChanged) {
                        zooKeeper.getData("/watcher2", this, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, null);
        zooKeeper.getData("/watcher2", new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("2");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeDataChanged) {
                        zooKeeper.getData("/watcher2", this, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }
}