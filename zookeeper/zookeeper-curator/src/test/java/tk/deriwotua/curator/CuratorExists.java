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
 * 通过Curator客户端判断节点是否存在
 */
public class CuratorExists {

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
                .namespace("get").build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 判断节点是否存在
     *  不存在时返回 stat 为null
     *
     * @throws Exception
     */
    @Test
    public void exists1() throws Exception {
        // 判断节点是否存在
        Stat stat = client.checkExists()
                // 节点路径
                .forPath("/node2");
        if(null != stat) {
            System.out.println(stat.getVersion());
        }
    }

    /**
     * 异步方式判断节点是否存在
     * @throws Exception
     */
    @Test
    public void exists2() throws Exception {
        // 异步方式判断节点是否存在
        client.checkExists()
                .inBackground(new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型：exists
                        System.out.println(curatorEvent.getType());
                        if(null != curatorEvent.getStat()) {
                            System.out.println(curatorEvent.getStat().getVersion());
                        }
                    }
                })
                .forPath("/node2");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}