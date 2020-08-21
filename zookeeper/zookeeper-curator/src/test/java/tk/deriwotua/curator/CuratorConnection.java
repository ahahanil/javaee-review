package tk.deriwotua.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;

/**
 * 通过Curator客户端连接到zookeeper
 *  通过CuratorFrameworkFactory.builder()工厂类构建CuratorFramework客户端
 *      IP地址端口号
 *      会话超时时间
 *      重连机制
 *      命名空间
 *      构建连接对象
 *  构建连接对象后通过客户端对象开启连接
 */
public class CuratorConnection {

    /**
     * 构建连接对象(连接中断后重连一次)
     * @return
     */
    private static CuratorFramework retryOneTime(){
        // 构建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                // IP地址端口号
                .connectString("127.0.0.1:2181")
                // 会话超时时间
                .sessionTimeoutMs(5000)
                // 重连机制
                // 与服务端连接断开三秒后尝试重连一次
                .retryPolicy(new RetryOneTime(3000))
                // 命名空间 通过CuratorFramework客户端创建节点时把命名空间作为父节点
                .namespace("create")
                // 构建连接对象
                .build();
        return client;
    }

    /**
     * 根据指定重连策略构建连接对象
     * @return
     */
    private static CuratorFramework buildWithRetry(RetryPolicy retryPolicy){
        // 构建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                // IP地址端口号
                .connectString("127.0.0.1:2181")
                // 会话超时时间
                .sessionTimeoutMs(5000)
                // 重连机制
                .retryPolicy(retryPolicy)
                // 命名空间 通过CuratorFramework客户端创建节点时把命名空间作为父节点
                .namespace("create")
                // 构建连接对象
                .build();
        return client;
    }


    public static void main(String[] args) {
        // baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount + 1)))
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        // 构建连接对象 重连策略：中断3s后重连一次
        //CuratorFramework client = retryOneTime();
        /**
         * 通过指定重连策略构建连接对象
         *  curator中session重连策略
         *      通过org.apache.curator.RetryPolicy#allowRetry()接口方法实现重连
         *      常用的四种实现
         *          org.apache.curator.retry.RetryOneTime   中断多少秒后重连1次,只重连1次
         *              3秒后重连一次，只重连1次
         *              RetryPolicy retryPolicy = new RetryOneTime(3000);
         *          org.apache.curator.retry.RetryNTimes    中断多少秒后重连1次,重连3次
         *              每3秒重连一次，重连3次
         *              RetryPolicy retryPolicy = new RetryNTimes(3,3000);
         *          org.apache.curator.retry.RetryUntilElapsed  中断多少秒后重连一次,总等待时间超过10秒后停止重连
         *              每3秒重连一次，总等待时间超过10秒后停止重连
         *              RetryPolicy retryPolicy=new RetryUntilElapsed(10000,3000);
         *          org.apache.curator.retry.ExponentialBackoffRetry    随着重连次数增加重连间隔也越来越长
         *              重连间隔计算：baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount + 1)))
         *              RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3); 重连三次重连间隔基于1s递增
         *
         */
        CuratorFramework client = buildWithRetry(retryPolicy);
        // 开启连接
        client.start();
        /**
         * 连接成功后通过client.isStarted()判断连接是否成功
         */
        System.out.println(client.isStarted());
        // 关闭连接
        client.close();
    }
}
