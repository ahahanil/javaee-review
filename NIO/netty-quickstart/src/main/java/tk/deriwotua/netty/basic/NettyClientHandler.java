package tk.deriwotua.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 客户端业务处理类
 *  自定义Handler要继承ChannelHandlerAdapter
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道就绪事件 当连接服务端成功后通道就进入就绪状态
     * @param ctx
     */
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client: " + ctx);

        /**
         * 根据指定的内容(可设置编码)创建一个缓冲区
         */
        ByteBuf buf = Unpooled.copiedBuffer("老板，还钱吧", CharsetUtil.UTF_8);
        /**
         * 通过上下文直接可以给服务端发送消息
         *  ChannelHandlerContext发送时会建立与服务端通道
         */
        ctx.writeAndFlush(buf);
    }

    /**
     * 读取数据事件   服务端发送数据后触发
     * @param ctx 上下文
     * @param msg 服务端返回的数据 缓冲区数据
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        // 数据转换字符串可指定编码
        System.out.println("服务器端发来的消息：" + buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕事件
     * @param ctx 上下文
     */
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 异常发生事件
     * @param ctx 上下文
     * @param cause 异常
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
