package tk.deriwotua.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务器端的业务处理类
 *  自定义Handler要继承ChannelHandlerAdapter
 *  这里从客户端读数据所以要继承ChannelInboundHandlerAdapter
 *      重写 channelRead channelReadComplete exceptionCaught
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件
     * @param ctx 上下文
     * @param msg 客户端发送过来的数据 缓冲区数据
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Server: " + ctx);
        // 缓冲区数据
        ByteBuf buf = (ByteBuf) msg;
        // 数据转换字符串可指定编码
        System.out.println("客户端发来的消息：" + buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕事件
     * @param ctx 上下文
     */
    public void channelReadComplete(ChannelHandlerContext ctx) {

        /**
         * 根据指定的内容(可设置编码)创建一个缓冲区
         */
        ByteBuf buf = Unpooled.copiedBuffer("就是没钱", CharsetUtil.UTF_8);
        /**
         * 通过上下文直接可以给客户端发送消息
         *  ChannelHandlerContext发送时会建立与客户端通道
         */
        ctx.writeAndFlush(buf);
    }

    /**
     * 异常发生事件
     * @param ctx 上下文
     * @param t
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        // 关闭上下文
        ctx.close();
    }

}
