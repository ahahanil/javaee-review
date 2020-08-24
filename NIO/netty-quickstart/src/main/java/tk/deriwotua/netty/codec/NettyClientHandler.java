package tk.deriwotua.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        /**
         * 发送 对象给服务端
         *  通过 protoc 生成的 .java内提供对象newBuilder方法创建对象
         *      newBuilder方法通过setXxx()方法设置值
         */
        BookMessage.Book book = BookMessage.Book.newBuilder().setId(1).setName("Java从入门到精通").build();
        // 发送前会根据ProtobufEncoder将对象编码为二进制字节码
        ctx.writeAndFlush(book);
    }

}

