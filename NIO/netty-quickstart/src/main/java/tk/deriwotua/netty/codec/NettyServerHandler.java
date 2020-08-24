package tk.deriwotua.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读数据
     *
     * @param ctx 上下文
     * @param msg 解码后的客户端发送对象
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 可以强转在于服务端会根据ProtobufEncoder将二进制字节码解码为对象
        BookMessage.Book book = (BookMessage.Book) msg;
        System.out.println("客户端发来数据：" + book.getName());
    }

}
