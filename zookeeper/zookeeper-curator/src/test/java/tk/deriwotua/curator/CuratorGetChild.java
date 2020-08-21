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

import java.util.List;

/**
 * 通过Curator客户端读取子节点
 */
public class CuratorGetChild {

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
                .build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 读取子节点数据
     * @throws Exception
     */
    @Test
    public void getChild1() throws Exception {
        // 读取子节点数据
        List<String> list = client.getChildren()
                // 节点路径
                .forPath("/get");
        for (String str : list) {
            System.out.println(str);
        }
    }

    /**
     * 异步方式读取子节点数据
     * @throws Exception
     */
    @Test
    public void getChild2() throws Exception {
        client.getChildren()
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
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型：children
                        System.out.println(curatorEvent.getType());
                        // 读取子节点数据
                        List<String> list=curatorEvent.getChildren();
                        for (String str : list) {
                            System.out.println(str);
                        }
                    }
                })
                .forPath("/get");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}