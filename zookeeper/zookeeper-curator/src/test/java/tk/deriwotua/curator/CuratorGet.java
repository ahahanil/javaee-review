package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 通过Curator客户端获取节点
 */
public class CuratorGet {

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
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                // 命名空间作为根节点
                .namespace("get").build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 读取节点 先创建/get/node1节点
     *
     * @throws Exception
     */
    @Test
    public void get1() throws Exception {
        // 读取节点数据
        byte[] bys = client.getData()
                // 节点的路径
                .forPath("/node1");
        // 返回时byte数组
        System.out.println(new String(bys));
    }

    /**
     * 读取节点时同时读取节点属性
     *
     * @throws Exception
     */
    @Test
    public void get2() throws Exception {
        // 读取数据时读取节点的属性
        Stat stat = new Stat();
        byte[] bys = client.getData()
                /**
                 * 读取属性
                 */
                .storingStatIn(stat)
                .forPath("/node1");
        System.out.println(new String(bys));
        System.out.println(stat.getVersion());
    }

    /**
     * 异步方式读取节点
     * @throws Exception
     */
    @Test
    public void get3() throws Exception {
        // 异步方式读取节点的数据
        client.getData()
                /**
                 * 异步方式读取数据
                 */
                .inBackground(new BackgroundCallback() {
                    /**
                     * 回调方法
                     * @param curatorFramework
                     * @param curatorEvent
                     * @throws Exception
                     */
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点的路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型：get_data
                        System.out.println(curatorEvent.getType());
                        // 数据
                        System.out.println(new String(curatorEvent.getData()));
                    }
                })
                .forPath("/node1");
        // 异步方式阻塞等待下
        Thread.sleep(5000);
        System.out.println("结束");
    }
}