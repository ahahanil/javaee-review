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
 * 通过Curator客户端更新节点
 */
public class CuratorSet {

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
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                // 命名空间自动作为根节点
                .namespace("set").build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 更新节点
     *  先命令行创建/set/node1节点
     *
     * @throws Exception
     */
    @Test
    public void set1() throws Exception {
        // 更新节点
        client.setData()
                // arg1:节点的路径
                // arg2:节点的数据
                .forPath("/node1", "node11".getBytes());
        System.out.println("结束");
    }

    /**
     * 乐观锁限定更新节点
     * @throws Exception
     */
    @Test
    public void set2() throws Exception {
        client.setData()
                /**
                 * 指定版本号
                 *      -1  版本号不参与更新条件
                 *      版本号不匹配时会报 BadVersionException
                 */
                .withVersion(2)
                .forPath("/node1", "node1111".getBytes());
        System.out.println("结束");
    }

    /**
     * 异步方式修改节点数据
     * @throws Exception
     */
    @Test
    public void set3() throws Exception {
        client.setData()
                .withVersion(-1)
                /**
                 * 异步方式更新节点
                 */
                .inBackground(new BackgroundCallback() {
                    /**
                     * 回调方法
                     * @param curatorFramework  Curator客户端
                     * @param curatorEvent  事件类型
                     * @throws Exception
                     */
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点的路径
                        System.out.println(curatorEvent.getPath());
                        // 事件的类型：set_data
                        System.out.println(curatorEvent.getType());
                    }
                }).forPath("/node1", "node1".getBytes());
        // 异步方式阻塞等待下
        Thread.sleep(5000);
        System.out.println("结束");
    }
}