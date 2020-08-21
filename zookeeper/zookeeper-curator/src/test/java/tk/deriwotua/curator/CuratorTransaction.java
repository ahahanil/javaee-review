package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Curator客户端事务机制
 */
public class CuratorTransaction {

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
                .namespace("create").build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 开启事务
     * @throws Exception
     */
    @Test
    public void tra1() throws Exception {
        // 创建节点
        client.create().forPath("/node1", "node1".getBytes());
        // 修改节点 修改时由于节点 /create/node2 是不存在的所以会报 NoNodeException
        client.setData().forPath("/node2", "node2".getBytes());
        /**
         * 执行上面后再命令行 分别执行 get /create/node1 是存在该节点的
         * 上面两步是不具备原子性的第二句是不会影响第一句
         *
         * 在某些业务中需要保证多个操作具有原子性要么全部成功要么全部失败
         */

        /**
         * curator开启事务 每步骤用and()连接
         *  开启事务后任何一步错误都会回滚事务
         */
        client.inTransaction()
                .create().forPath("/node1","node1".getBytes())
                .and()
                .create().forPath("/node2","node2".getBytes())
                .and()
                //事务提交
                .commit();
    }
}