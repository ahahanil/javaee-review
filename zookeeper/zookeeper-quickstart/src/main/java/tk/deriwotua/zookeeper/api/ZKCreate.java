package tk.deriwotua.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKCreate {

    String IP = "127.0.0.1:2181";
    ZooKeeper zooKeeper;

    @Before
    public void before() throws Exception {
        // 计数器对象
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        /**
         * arg1:服务器的ip和端口
         * arg2:客户端与服务器之间的会话超时时间  以毫秒为单位的
         * arg3:监视器对象
         */
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                }
            }
        });
        // 主线程阻塞等待连接对象的创建成功
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        zooKeeper.close();
    }

    /**
     * world授权模式所有用户cdrwa权限 持久化节点
     *
     * @throws Exception
     */
    @Test
    public void create1() throws Exception {
        /**
         * 命令行
         *  create /create/node1 "node1"
         *  setAcl /create/node1 world:anyone:cdrwa
         * API方式
         *  arg1:节点的路径
         *  arg2:节点的数据
         *  arg3:权限列表  world:anyone:cdrwa -> ZooDefs.Ids.OPEN_ACL_UNSAFE
         *  arg4:节点类型  持久化节点
         */
        zooKeeper.create("/create/node1", "node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * world授权模式所有用户r权限 持久化节点
     *
     * @throws Exception
     */
    @Test
    public void create2() throws Exception {
        /**
         * 命令行
         *  create /create/node2 "node2"
         *  setAcl /create/node2 world:anyone:r -> ZooDefs.Ids.READ_ACL_UNSAFE
         */
        zooKeeper.create("/create/node2", "node2".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * world授权模式所有用户自定义权限 持久化节点
     *
     * @throws Exception
     */
    @Test
    public void create3() throws Exception {
        /**
         * 命令行
         *  create /create/node3 "node3"
         *  setAcl /create/node3 world:anyone:rw
         */

        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("world", "anyone");
        // 权限设置
        /**
         * ACL 对象构造方法两个参数
         *      perms int类型参数   描述cdrwa权限信息
         *      id  Id类型参数      针对与当前权限的授权模式和授权对象
         */
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        acls.add(new ACL(ZooDefs.Perms.WRITE, id));
        zooKeeper.create("/create/node3", "node3".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * IP授权模式 127.0.0.1 对象 cdrwa所有权限
     *
     * @throws Exception
     */
    @Test
    public void create4() throws Exception {
        /**
         * 命令行
         *  create /create/node4 "node4"
         *  setAcl /create/node4 IP:127.0.0.1:cdrwa
         */

        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("ip", "127.0.0.1");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node4", "node4".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * auth授权模式deriwotua用户cdrwa所有权限
     *
     * @throws Exception
     */
    @Test
    public void create5() throws Exception {
        /**
         * 命令行
         *  create /create/node5 "node5"
         *  addauth digest deriwotua:123456
         *  setAcl /create/node5 auth:deriwotua:cdrwa
         */

        // auth授权模式首先需要添加授权用户
        zooKeeper.addAuthInfo("digest", "deriwotua:123456".getBytes());
        zooKeeper.create("/create/node5", "node5".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    }

    /**
     * auth授权模式deriwotua用户自定义权限
     *
     * @throws Exception
     */
    @Test
    public void create6() throws Exception {
        /**
         * 命令行
         *  create /create/node6 "node6"
         *  addauth digest deriwotua:123456
         *  setAcl /create/node6 auth:deriwotua:r
         */

        //auth授权模式首先需要添加授权用户
        zooKeeper.addAuthInfo("digest", "deriwotua:123456".getBytes());
        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("auth", "deriwotua");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zooKeeper.create("/create/node6", "node6".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * digest授权模式
     *
     * @throws Exception
     */
    @Test
    public void create7() throws Exception {
        /**
         * 命令行
         *  create /create/node7 "node7"
         *  setAcl /create/node7 digest:deriwotua:qlzQzCLKhBROghkooLvb+Mlwv4A=
         */

        // 权限列表
        List<ACL> acls = new ArrayList<ACL>();
        // 授权模式和授权对象
        Id id = new Id("digest", "deriwotua:qlzQzCLKhBROghkooLvb+Mlwv4A=");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node7", "node7".getBytes(), acls, CreateMode.PERSISTENT);
    }

    /**
     * 持久化顺序节点 CreateMode.PERSISTENT_SEQUENTIAL
     *
     * @throws Exception
     */
    @Test
    public void create8() throws Exception {
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        // 顺序节点会自动拼接自增序号
        String result = zooKeeper.create("/create/node8", "node8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(result);
    }

    /**
     * 临时节点 CreateMode.EPHEMERAL
     *
     * @throws Exception
     */
    @Test
    public void create9() throws Exception {
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        // 临时节点创建后关闭会话即消失
        String result = zooKeeper.create("/create/node9", "node9".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    /**
     * 临时顺序节点 CreateMode.EPHEMERAL_SEQUENTIAL
     *
     * @throws Exception
     */
    @Test
    public void create10() throws Exception {
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node10", "node10".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(result);
    }

    /**
     * 异步方式创建节点
     * 默认同步方式创建节点时create()方法执行后即会自动处理返回创建信息
     * 而异步方法创建节点时create()方法执行后并不会立即收到创建成功或失败的消息而是需要自定义回调方法监听推送的消息
     *
     * @throws Exception
     */
    @Test
    public void create11() throws Exception {
        // 异步方式创建节点
        zooKeeper.create("/create/node11", "node11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                new AsyncCallback.StringCallback() {
                    /**
                     * 异步创建节点时监听到服务器返回的消息会回调processResult处理
                     * @param rc 0 代表创建成功
                     * @param path 节点的路径
                     * @param ctx 节点的路径
                     * @param name 上下文参数
                     */
                    public void processResult(int rc, String path, Object ctx, String name) {
                        // 0 代表创建成功
                        System.out.println(rc);
                        // 节点的路径
                        System.out.println(path);
                        // 节点的路径
                        System.out.println(name);
                        // 上下文参数
                        System.out.println(ctx);
                    }
                }, "I am context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
