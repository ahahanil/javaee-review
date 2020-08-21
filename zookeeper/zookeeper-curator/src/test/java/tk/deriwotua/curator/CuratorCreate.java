package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过Curator客户端创建节点
 */
public class CuratorCreate {

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
        // 构建CuratorFramework客户端
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                // 指定命名空间后命名空间作为根节点
                .namespace("create")
                .build();
        // 建立连接
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    /**
     * 创建持久化节点
     * @throws Exception
     */
    @Test
    public void create1() throws Exception {
        // 新增节点
        client.create()
                // 节点的类型 持久化节点
                .withMode(CreateMode.PERSISTENT)
                // 节点的权限列表 world:anyone:cdrwa
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                // arg1:节点的路径
                // arg2:节点的数据
                .forPath("/node1", "node1".getBytes());
        System.out.println("结束");
    }

    /**
     * 创建自定义权限列表节点
     * @throws Exception
     */
    @Test
    public void create2() throws Exception {
        // 自定义权限列表
        // 权限列表
        List<ACL> list = new ArrayList<ACL>();
        // IP授权模式和授权对象
        Id id = new Id("ip", "192.168.60.130");
        list.add(new ACL(ZooDefs.Perms.ALL, id));
        client.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/node2", "node2".getBytes());
        System.out.println("结束");
    }

    /**
     * 递归创建节点树
     * @throws Exception
     */
    @Test
    public void create3() throws Exception {
        /**
         * 创建 /create/node3/node31 节点
         *      由于 /create/node3节点不存在所以
         *      这里会报 NoNodeException
         */
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node3/node31", "node31".getBytes());

        /**
         * 通过creatingParentsIfNeeded递归方式创建节点如果父节点不存在先自动创建父节点
         */
        client.create()
                // 递归节点的创建
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node3/node31", "node31".getBytes());
        System.out.println("结束");
    }

    /**
     * 异步方式创建节点
     * @throws Exception
     */
    @Test
    public void create4() throws Exception {
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                /**
                 * 异步回调接口 通过实现BackgroundCallback#processResult()方法
                 */
                .inBackground(new BackgroundCallback() {
                    /**
                     *  异步回调接口
                     * @param curatorFramework 客户端与服务端建立连接对象
                     * @param curatorEvent  事件对象
                     * @throws Exception
                     */
                     public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                         // 获取触发事件节点的路径 这里打印的path是不带命名空间实际节点是命名空间作为父节点
                         System.out.println(curatorEvent.getPath());
                         // 事件类型：create
                         System.out.println(curatorEvent.getType());
                     }
                })
                .forPath("/node4","node4".getBytes());
        //异步方式创建节点 这里稍微阻塞等待下
        Thread.sleep(5000);
        System.out.println("结束");
    }
}