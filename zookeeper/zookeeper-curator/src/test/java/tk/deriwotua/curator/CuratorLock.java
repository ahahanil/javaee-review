package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Curator客户端提供的两种分布式锁
 *  当两个排他锁对像指向同一个路径时代表这两个排他锁对象是持有同一把锁的
 */
public class CuratorLock {

    /**
     * zookeeper服务器地址 集群部署各服务器节点地址之间逗号分隔
     */
    String IP = "127.0.0.1:2181";
    CuratorFramework client;

    @Before
    public void before() {
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
     * 排他锁
     *  同时执行两次此测试用例
     *      首先启动的后台先获得锁然后打印数字
     *      后启动的此时一致处于等待获取锁阻塞状态
     *
     * @throws Exception
     */
    @Test
    public void lock1() throws Exception {
        /**
         * 排他锁
         * arg1:连接对象
         * arg2:节点路径
         */
        InterProcessLock interProcessLock = new InterProcessMutex(client, "/lock1");
        System.out.println("等待获取锁对象!");
        // 获取锁
        interProcessLock.acquire();
        // 执行业务逻辑
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        // 释放锁
        interProcessLock.release();
        System.out.println("等待释放锁!");
    }

    /**
     * 读写锁
     *  获取的是读锁  读锁是可共享资源的所以可并行执行的
     *  此时同时执行两次此测试用例
     *      无论哪个先启动后启动的也可以获得锁执行打印
     *
     * @throws Exception
     */
    @Test
    public void lock2() throws Exception {
        // 读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        // 获取读锁对象
        InterProcessLock interProcessLock = interProcessReadWriteLock.readLock();
        System.out.println("等待获取锁对象!");
        // 获取锁
        interProcessLock.acquire();
        // 执行业务逻辑
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        // 释放锁
        interProcessLock.release();
        System.out.println("等待释放锁!");
    }

    /**
     * 读写锁
     *  获取的是写锁
     *      在上面 lock2() 获取读锁后执行的同时启动此测试用例会发现 lock3() 处于等待获取写锁对象；在 lock2() 释放读锁后 lock3() 才能获取到写锁
     *      同理在 lock3() 获取写锁后执行的同时启动此上面 lock2() 测试用例会发现 lock2() 处于等待获取读锁对象；在 lock3() 释放写锁后 lock2() 才能获取到读锁
     *      同时执行两次 lock3() 测试用例 由于是写锁自然也是排他的
     * @throws Exception
     */
    @Test
    public void lock3() throws Exception {
        // 读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        // 获取写锁对象
        InterProcessLock interProcessLock = interProcessReadWriteLock.writeLock();
        System.out.println("等待获取锁对象!");
        // 获取锁
        interProcessLock.acquire();
        // 执行业务逻辑
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        // 释放锁
        interProcessLock.release();
        System.out.println("等待释放锁!");
    }

}