package tk.deriwotua.rpc.serverStub;

import java.io.Serializable;

/**
 * 封装类信息
 *  描述需要调用服务方哪个接口哪个方法
 *  RPC网络传输的数据
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 远程调用类名或接口类名
     */
    private String className;
    /**
     * 远程调用的方法名
     */
    private String methodName;
    /**
     * 远程调用方法的参数类型
     */
    private Class<?>[] types;
    /**
     * 远程调用方法的参数列表
     */
    private Object[] objects;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}

