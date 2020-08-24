package tk.deriwotua.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义一个服务器端业务处理类
 *  自定义Handler要继承ChannelHandlerAdapter
 *      之前继承的是 ChannelInboundHandler 直接操作缓冲区 手动从缓冲区设置编码取出数据
 *      这里继承 SimpleChannelInboundHandler 可以指定泛型类客户端发送的数据自动转指定类型
 *          指定的泛型对应读取时函数上泛型类型
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 缓存连接到服务端的通道(就绪通道列表)
     */
    public static List<Channel> channels = new ArrayList<>();

    /**
     * 通道就绪
     *
     * @param ctx 上下文
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 获取与客户端通道
        Channel inChannel = ctx.channel();
        // 缓存连接到服务端的通道
        channels.add(inChannel);
        // 打印远程客户端
        System.out.println("[Server]:" + inChannel.remoteAddress().toString().substring(1) + "上线");
    }

    /**
     * 通道未就绪(中断)
     *
     * @param ctx 上下文
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 获取与客户端通道
        Channel inChannel = ctx.channel();
        // 中断后移除
        channels.remove(inChannel);
        System.out.println("[Server]:" + inChannel.remoteAddress().toString().substring(1) + "离线");
    }

    /**
     * 读取数据
     *
     * @param ctx 上下文
     * @param s 继承类泛型类型 客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        Channel inChannel = ctx.channel();
        // 模仿数据广播
        for (Channel channel : channels) {
            if (channel != inChannel) {
                channel.writeAndFlush("[" + inChannel.remoteAddress().toString().substring(1) + "]" + "说：" + s + "\n");
            }
        }
    }

}
