package tk.deriwotua.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * 聊天程序客户端
 */
public class ChatClient {

    /**
     * 服务器端IP地址
     */
    private final String host;
    /**
     * 服务器端端口号
     */
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        // 客户端线程组 处理读写
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 客户端的启动助手配置参数
            Bootstrap bootstrap = new Bootstrap()
                    // 设置线程组
                    .group(group)
                    // 设置客户端通道的实现类
                    .channel(NioSocketChannel.class)
                    // 通过通道初始化对象把自定义的handler添加到Pipeline链中
                    .handler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 初始化通道
                         * @param ch 通道类型
                         * @throws Exception
                         */
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // Pipeline链
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
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });
            // 启动客户端去连接服务器端 connect内部异步连接
            // 通过sync同步阻塞等待连接(同步意义在于连接不成功后面逻辑也没有意义)
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            Channel channel = cf.channel();
            System.out.println("------" + channel.localAddress().toString().substring(1) + "------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 发送数据
                channel.writeAndFlush(msg + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatClient("127.0.0.1", 9999).run();
    }
}
