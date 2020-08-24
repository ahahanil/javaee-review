package tk.deriwotua.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) {
                        // 指定客户端发送给服务端数据通过ProtobufEncoder编码器编码
                        sc.pipeline().addLast("encoder",new ProtobufEncoder());
                        sc.pipeline().addLast(new NettyClientHandler());
                    }
                });

        // 启动客户端
        ChannelFuture cf = b.connect("127.0.0.1", 9999).sync(); // (5)

        // 等待连接关闭
        cf.channel().closeFuture().sync();
    }
}
