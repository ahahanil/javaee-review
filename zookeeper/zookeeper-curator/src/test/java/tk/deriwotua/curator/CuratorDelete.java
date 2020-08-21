package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 通过Curator客户端删除节点
 */
public class CuratorDelete {

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
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                // 命名空间作为根节点
                .namespace("delete").build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 删除节点 先创建 /delete/node1节点
     *
     * @throws Exception
     */
    @Test
    public void delete1() throws Exception {
        // 删除节点
        client.delete()
                // 节点的路径
                .forPath("/node1");
        System.out.println("结束");
    }

    /**
     * 乐观锁版本号限定
     * @throws Exception
     */
    @Test
    public void delete2() throws Exception {
        client.delete()
                /**
                 * 指定版本号
                 *      -1  版本号不参与更新条件
                 *      版本号不匹配时会报 BadVersionException
                 */
                .withVersion(0)
                .forPath("/node1");
        System.out.println("结束");
    }

    /**
     * 删除包含子节点的节点
     * @throws Exception
     */
    @Test
    public void delete3() throws Exception {
        /**
         * 节点删除时如果节点存在子节点时无法直接删除的
         *      会报 NotEmptyException
         */
        /*client.delete()
                // 版本号不参与删除条件
                .withVersion(-1)
                .forPath("/node1");*/
        /**
         * deletingChildrenIfNeeded方法删除节点时存在子节点子节点也一并删除
         */
        client.delete()
                .deletingChildrenIfNeeded()
                // 版本号不参与删除条件
                .withVersion(-1)
                .forPath("/node1");
        System.out.println("结束");
    }

    /**
     * 异步方式删除节点
     * @throws Exception
     */
    @Test
    public void delete4() throws Exception {
        // 异步方式删除节点
        client.delete()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                /**
                 * 异步方式删除节点
                 */
                .inBackground(new BackgroundCallback() {
                    /**
                     * 回调方法
                     * @param curatorFramework
                     * @param curatorEvent
                     * @throws Exception
                     */
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型
                        System.out.println(curatorEvent.getType());
                    }
                })
                .forPath("/node1");
        // 异步方式阻塞等待下
        Thread.sleep(5000);
        System.out.println("结束");
    }
}