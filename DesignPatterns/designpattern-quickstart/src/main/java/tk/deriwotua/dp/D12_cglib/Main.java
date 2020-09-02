package tk.deriwotua.dp.D12_cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * JDK动态代理依赖接口(底层ASM)
 * CGLIB动态代理基于父类(底层ASM)
 *  需要添加cglib依赖
 */
public class Main {
    public static void main(String[] args) {
        // 创建增强器
        Enhancer enhancer = new Enhancer();
        // 设置目标对象为父类
        enhancer.setSuperclass(Tank.class);
        /**
         * 指定拦截器
         *  相当于JDK动态代理生成代理对象调用代理方法内调用的InvocationHandler#invoke()方法
         */
        enhancer.setCallback(new TimeMethodInterceptor());
        // 生成代理对象
        Tank tank = (Tank)enhancer.create();
        tank.move();
    }
}

/**
 * 生成代理对象调用代理方法时拦截器
 */
class TimeMethodInterceptor implements MethodInterceptor {

    /**
     * 类似InvocationHandler#invoke()方法
     * @param o 自动生成的动态代理对象(目标对象子类)
     *          所以当目标对象被 final 修饰时是没办法通过cglib动态生成子类即无法生成代理对象
     * @param method 代理方法
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println(o.getClass().getSuperclass().getName());
        System.out.println("before");
        Object result = null;
        result = methodProxy.invokeSuper(o, objects);
        System.out.println("after");
        return result;
    }
}

/**
 * 目标对象
 */
class Tank {
    public void move() {
        System.out.println("Tank moving claclacla...");
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


