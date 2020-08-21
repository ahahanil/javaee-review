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
 * getChildren()方法可以捕获节点两种事件
 *      NodeChildrenChanged:子节点发生变化
 *      NodeDeleted:节点删除
 */
public class ZKWatcherGetChild {
    String IP = "127.0.0.1:2181";
    ZooKeeper zooKeeper = null;

    @Before
    public void before() throws IOException, InterruptedException {
        final CountDownLatch connectedSemaphore = new CountDownLatch(1);
        // 连接zookeeper客户端
        zooKeeper = new ZooKeeper(IP, 6000,
                /**
                 * 创建连接时指定匿名内部类监听器对象
                 */
                new Watcher() {
                    public void process(WatchedEvent event) {
                        System.out.println("连接对象的参数!");
                        // 连接成功
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            connectedSemaphore.countDown();
                        }
                        System.out.println("path=" + event.getPath());
                        System.out.println("eventType=" + event.getType());
                    }
                });
        connectedSemaphore.await();
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }

    /**
     * 通过getChildren方法给节点注册连接创建时指定匿名内部类监听器对象
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetChild1() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:使用连接对象中的watcher
         */
        zooKeeper.getChildren("/watcher3", true);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 通过getChildren方法给节点注册自定义匿名内部类监听器对象
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetChild2() throws KeeperException, InterruptedException {
        /**
         * arg1:节点的路径
         * arg2:自定义watcher
         */
        zooKeeper.getChildren("/watcher3", new Watcher() {
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
     * 通过getChildren方法给节点注册Watcher实现类监听器对象(一次性)
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetChild3() throws KeeperException, InterruptedException {
        // 一次性
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("自定义watcher");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getChildren("/watcher3", this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        zooKeeper.getChildren("/watcher3", watcher);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    /**
     * 通过getChildren方法给节点注册多个监听器
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void watcherGetChild4() throws KeeperException, InterruptedException {
        // 多个监视器对象
        zooKeeper.getChildren("/watcher3", new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("1");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getChildren("/watcher3", this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        zooKeeper.getChildren("/watcher3", new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    System.out.println("2");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getChildren("/watcher3", this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
