package tk.deriwotua.dp.D12_proxy.v08;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * 问题：我想记录坦克的移动时间
 * 最简单的办法：修改代码，记录时间
 * 问题2：如果无法改变方法源码呢？
 * 用继承？
 * v05:使用代理
 * v06:代理有各种类型
 * 问题：如何实现代理的各种组合？继承？Decorator?
 * v07:代理的对象改成Movable类型-越来越像decorator了
 * v08:如果有stop方法需要代理...
 * 如果想让LogProxy可以重用，不仅可以代理Tank，还可以代理任何其他可以代理的类型 Object
 *      要实现可代理一切难点在于不清楚代理对象所需代理的方法及其参数列表即动态代理
 *  分离代理行为与被代理对象
 *  使用jdk的动态代理
 *      在之前实现代理对象时还编写相应类的代理对象类在动态代理里就不需要了直接代码动态生成代理对象
 */
public class Tank implements Movable {

    /**
     * 模拟坦克移动了一段儿时间
     */
    @Override
    public void move() {
        System.out.println("Tank moving claclacla...");
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Tank tank = new Tank();

        /**
         * reflection反射通过二进制字节码分析类的属性和方法动态生成代理对象
         * JDK动态代理针对接口生成代理对象
         *  arg1 代理对象加载到内存的ClassLoader(通常和目标对象保持一致)
         *  arg2 代理对象实现接口(目标对象所实现的接口)
         */
        Movable m = (Movable)Proxy.newProxyInstance(Tank.class.getClassLoader(),
                new Class[]{Movable.class}, //tank.class.getInterfaces()
                /**
                 * 被代理对象方法执行时处理器
                 */
                new LogHandler(tank)
        );

        /**
         * JDK动态代理会自动生成代理对象该代理对象也会实现目标对象的接口
         * 即自动生成的代理对象会继承Movable接口实现Movable#move() 所以这里代理对象m可以调用move()本身就是Movable实现类
         * 在自动生成的代理对象实现的Movable#move()方法里会调用Proxy#newProxyInstance(,, java.lang.reflect.InvocationHandler)时
         * 传递的InvocationHandler接口参数的InvocationHandler#invoke()方法
         * 所以这里执行m.move() 会调用 LogHandler#invoke()方法
         *     这个过程可以通过反编译自动生成的代理对象查看源码看到
         */
        m.move();
    }
}

/**
 * 代理对象
 *  被代理对象方法执行时处理器
 */
class LogHandler implements InvocationHandler {

    Tank tank;

    public LogHandler(Tank tank) {
        this.tank = tank;
    }
    //getClass.getMethods[]

    /**
     * 被代理对象方法执行时动态指定逻辑
     * @param proxy 自动生成代理对象
     * @param method 被代理方法
     * @param args 方法参数列表
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method " + method.getName() + " start..");
        /**
         * Movable可能有多个实现类需要指定调用哪个实现类的method方法
         *  这里即 Tank.move()
         */
        Object o = method.invoke(tank, args);
        System.out.println("method " + method.getName() + " end!");
        return o;
    }
}

interface Movable {
    void move();
}