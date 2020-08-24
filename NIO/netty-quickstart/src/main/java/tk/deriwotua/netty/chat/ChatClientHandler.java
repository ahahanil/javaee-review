package tk.deriwotua.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 自定义一个客户端业务处理类
 *  自定义Handler要继承ChannelHandlerAdapter
 *      之前继承的是 ChannelInboundHandler 直接操作缓冲区 手动从缓冲区设置编码取出数据
 *      这里继承 SimpleChannelInboundHandler 可以指定泛型类客户端发送的数据自动转指定类型
 *          指定的泛型对应读取时函数上泛型类型
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 读取服务端数据
     *
     * @param ctx 上下文
     * @param s 继承类泛型类型 客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println(s.trim());
    }
}