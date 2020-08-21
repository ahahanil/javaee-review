package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Curator客户端监听节点变化
 */
public class CuratorWatcher {

    /**
     * zookeeper服务器地址 集群部署各服务器节点地址之间逗号分隔
     */
    String IP = "127.0.0.1:2181";
    CuratorFramework client;

    @Before
    public void before() {
        /**
         * 重连三次重连间隔基于1s递增
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory
                .builder()
                .connectString(IP)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * NodeCache监视某个节点的数据变化
     *  执行此测试用例后开始监视节点
     *      命令行执行 create /watcher1 "watcher1" 对应的输出
     *          /watcher1
     *          watcher1
     *      当命令行再次执行 set /watcher1 "watcher11" 监视器这边后台一样会捕获到
     *          /watcher1
     *          watcher11
     *      说明Curator客户端的Watcher是非一次性的注册后一致生效
     *
     * @throws Exception
     */
    @Test
    public void watcher1() throws Exception {
        /**
         * NodeCache监视某个节点的数据变化
         *  arg1:连接对象
         *  arg2:监视的节点路径
         */
        final NodeCache nodeCache = new NodeCache(client, "/watcher1");
        // 启动监视器对象监视节点
        nodeCache.start();
        /**
         * NodeCache监视器通过注册NodeCacheListener重写`NodeCacheListener#nodeChanged()`捕获节点相应变化
         */
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            /**
             * 节点变化时回调的方法
             * @throws Exception
             */
            public void nodeChanged() throws Exception {
                //当前节点路径
                System.out.println(nodeCache.getCurrentData().getPath());
                //当前节点数据
                System.out.println(new String(nodeCache.getCurrentData().getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        //关闭监视器对象
        nodeCache.close();
    }

    /**
     * 通过PathChildrenCache监视子节点变化
     *  然后执行此测试用例开始监视节点
     *      命令行执行 create /watcher1/node1 "node1" 对应的输出
     *          CHILD_ADDED
     *          /watcher1/node1
     *          "node1"
     *      当命令行执行 set /watcher1/node1 "node11" 监视器这边后台一样会捕获到
     *          CHILD_UPDATED
     *          /watcher1/node1
     *          "node11"
     *      当命令行执行 delete /watcher1/node1   监视器这边后台一样会捕获到
     *          CHILD_REMOVED
     *          /watcher1/node1
     *          "node11"
     * @throws Exception
     */
    @Test
    public void watcher2() throws Exception {
        /**
         * 监视子节点的变化
         * arg1:连接对象
         * arg2:监视的节点路径
         * arg3:事件中是否可以获取节点的数据
         */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", true);
        // 启动监听
        pathChildrenCache.start();
        /**
         * pathChildrenCache监视器通过注册PathChildrenCacheListener重写`PathChildrenCacheListener#childEvent()`捕获子节点相应变化
         */
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * 当子节点方法变化时回调的方法
             * @param curatorFramework
             * @param pathChildrenCacheEvent
             * @throws Exception
             */
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                // 节点的事件类型
                System.out.println(pathChildrenCacheEvent.getType());
                // 节点的路径
                System.out.println(pathChildrenCacheEvent.getData().getPath());
                // 节点数据
                System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        // 关闭监听
        pathChildrenCache.close();
    }

    /**
     * 通过PathChildrenCache监视子节点变化(设置不可获取节点的数据)
     *  然后执行此测试用例开始监视节点
     *      命令行执行 create /watcher1/node1 "node1" 对应的输出
     *          CHILD_ADDED
     *          /watcher1/node1       # 这里就不会在打印节点数据
     *      当命令行执行 set /watcher1/node1 "node11" 监视器这边后台一样会捕获到
     *          CHILD_UPDATED
     *          /watcher1/node1
     *          "node11"
     *      当命令行执行 delete /watcher1/node1   监视器这边后台一样会捕获到
     *          CHILD_REMOVED
     *          /watcher1/node1
     *          "node11"
     * @throws Exception
     */
    @Test
    public void watcher3() throws Exception {
        /**
         * 监视子节点的变化
         * arg1:连接对象
         * arg2:监视的节点路径
         * arg3:事件中是否可以获取节点的数据
         */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", false);
        // 启动监听
        pathChildrenCache.start();
        /**
         * pathChildrenCache监视器通过注册PathChildrenCacheListener重写`PathChildrenCacheListener#childEvent()`捕获子节点相应变化
         */
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            /**
             * 当子节点方法变化时回调的方法
             * @param curatorFramework
             * @param pathChildrenCacheEvent
             * @throws Exception
             */
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                // 节点的事件类型
                System.out.println(pathChildrenCacheEvent.getType());
                // 节点的路径
                System.out.println(pathChildrenCacheEvent.getData().getPath());
                // 节点数据 因为上面设置的是不可获取节点数据所以这里数据为空
                System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        // 关闭监听
        pathChildrenCache.close();
    }
}