package tk.deriwotua.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务端
 *  定义接收连接和处理具体业务两个线程组
 *  通过服务器端启动助手来配置参数
 *      设置服务端通道实现方式
 *      设置等待时线程队列
 *      连接是否保持活动状态
 *      把自定义的handler添加到Pipeline链中
 *          通过通道初始化对象设置
 *  绑定服务端端口
 *  关闭通道，关闭线程组
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {

        //1. 创建一个 接收客户端连接 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2. 创建一个 处理网络操作 线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3. 创建服务器端启动助手来配置参数
        ServerBootstrap b = new ServerBootstrap();
        // 4.设置两个线程组
        b.group(bossGroup, workerGroup)
                // 5.使用NioServerSocketChannel作为服务器端通道的实现
                .channel(NioServerSocketChannel.class)
                // 6.设置线程队列中等待连接的个数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 7.保持连接活动状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 通过通道初始化对象把自定义的handler添加到Pipeline链中
                // 8.创建一个通道初始化对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    /**
                     * 初始化通道
                     * @param sc 通道类型
                     */
                    public void initChannel(SocketChannel sc) {
                        // 9. 往Pipeline链中添加自定义的handler类
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("......Server config is ready......");
        //10. 绑定端口 bind方法会开启线程异步绑定端口
        // 通过sync同步阻塞等待绑定端口(同步意义在于绑定不成功后面逻辑也没有意义)
        ChannelFuture cf = b.bind(9999).sync();
        System.out.println("......Server is starting......");

        //11. 关闭通道，关闭线程组
        cf.channel().closeFuture().sync(); //异步
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
