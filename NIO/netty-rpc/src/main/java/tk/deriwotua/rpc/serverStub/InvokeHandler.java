package tk.deriwotua.rpc.serverStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 服务器端业务处理类
 *  接收数据
 *  解码
 *  反射调用本地方法
 *  封装响应
 *  编码
 *  发送
 */
public class InvokeHandler extends ChannelInboundHandlerAdapter {
    /**
     * 得到某接口下某个实现类的名字
     * @param classInfo RPC网络传输的数据 描述需要调用服务方哪个接口哪个方法
     * @return
     * @throws Exception
     */
    private String getImplClassName(ClassInfo classInfo) throws Exception {
        //服务方接口和实现类所在的包路径
        String interfacePath = "tk.deriwotua.rpc.server";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class superClass = Class.forName(interfacePath + interfaceName);
        Reflections reflections = new Reflections(interfacePath);
        //得到某接口下的所有实现类
        Set<Class> implClassSet = reflections.getSubTypesOf(superClass);
        if (implClassSet.size() == 0) {
            System.out.println("未找到实现类");
            return null;
        } else if (implClassSet.size() > 1) {
            System.out.println("找到多个实现类，未明确使用哪一个");
            return null;
        } else {
            //把集合转换为数组
            Class[] classes = implClassSet.toArray(new Class[0]);
            return classes[0].getName(); //得到实现类的名字
        }
    }

    /**
     * 读取客户端发来的数据并通过反射调用实现类的方法
     * @param ctx 上下文
     * @param msg 远程调用方发送过来的 ClassInfo
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        //通过反射调用实现类的方法
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }
}

