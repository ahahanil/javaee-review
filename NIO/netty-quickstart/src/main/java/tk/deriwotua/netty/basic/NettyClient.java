package tk.deriwotua.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty客户端
 *  定义线程组
 *  通过客户端的启动助手配置参数
 *      设置服务端通道实现方式
 *      把自定义的handler添加到Pipeline链中
 *          通过通道初始化对象设置
 *  启动客户端去连接服务器端
 *  关闭通道，关闭线程组
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {

        // 1. 创建一个线程组 处理读写
        EventLoopGroup group = new NioEventLoopGroup();
        // 2. 创建客户端的启动助手，完成相关配置
        Bootstrap b = new Bootstrap();
        // 3. 设置线程组
        b.group(group)
                // 4. 设置客户端通道的实现类
                .channel(NioSocketChannel.class)
                // 通过通道初始化对象把自定义的handler添加到Pipeline链中
                // 5. 创建一个通道初始化对象
                .handler(new ChannelInitializer<SocketChannel>() {
                    /**
                     * 初始化通道
                     * @param socketChannel
                     * @throws Exception
                     */
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //6.往Pipeline链中添加自定义的handler
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("......Client is config ready......");

        //7.启动客户端去连接服务器端  connect方法开启线程异步连接
        // 通过sync同步阻塞等待连接(同步意义在于连接不成功后面逻辑也没有意义)
        // 连接成功后通道就进入就绪状态
        ChannelFuture cf = b.connect("127.0.0.1", 9999).sync();

        //8.关闭连接(异步非阻塞)
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
