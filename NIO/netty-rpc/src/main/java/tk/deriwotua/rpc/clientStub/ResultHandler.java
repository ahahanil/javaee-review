package tk.deriwotua.rpc.clientStub;

import io.netty.channel.*;

/**
 * 客户端业务处理类
 */
public class ResultHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    public Object getResponse() {
        return response;
    }

    /**
     * 读取服务器端返回的数据(远程调用的结果)
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        ctx.close();
    }
}

