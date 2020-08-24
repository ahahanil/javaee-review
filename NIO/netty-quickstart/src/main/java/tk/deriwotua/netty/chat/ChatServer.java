package tk.deriwotua.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 聊天程序服务器端
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
public class ChatServer {

    /**
     * 服务器端端口号
     */
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 接收客户端连接 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理网络操作 线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务器端启动助手配置参数
            ServerBootstrap b = new ServerBootstrap();
            // 设置两个线程组
            b.group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务器端通道的实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列中等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 保持连接活动状态 长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 通过通道初始化对象把自定义的handler添加到Pipeline链中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 初始化通道
                         * @param ch 通道类型
                         */
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // pipeline链
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * 自定义handler继承 SimpleChannelInboundHandler<T> 指定泛型
                             *  能够自动将缓冲区数据转换为指定类型
                             *      网络数据传输中使用的二进制字节码但数据发送时都是对象
                             *          发送时需要编码器将对象转换为二进制字节码
                             *          接收时需要解码器将二进制字节码转换为对象
                             *
                             * 自定义handler泛型是String所以这里需要String编码解码器
                             * 这里还需要注意设置编码解码器要在设置自定义handler之前
                             *      也很好理解 Pipeline链式要先编码解码后业务处理类处理
                             */
                            // 往pipeline链中添加一个解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 往pipeline链中添加一个编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 往pipeline链中添加自定义的handler(业务处理类)
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });
            System.out.println("Netty Chat Server启动......");
            // 绑定端口 bind开启线程异步绑定端口
            // 通过sync同步阻塞等待绑定端口(同步意义在于绑定不成功后面逻辑也没有意义)
            ChannelFuture f = b.bind(port).sync();
            // 关闭通道
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程组
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("Netty Chat Server关闭......");
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatServer(9999).run();
    }
}