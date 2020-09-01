package tk.deriwotua.dp.D11_flyweight.simpleflyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂
 *  客户端不可以直接将具体享元类实例化，而必须通过一个工厂对象
 *  通常享元工厂对象在整个系统中只有一个，因此也可以使用单例模式。
 */
public class FlyweightFactory {
    /**
     * 缓存用于共享
     */
    private Map<Character, Flyweight> files = new HashMap<Character, Flyweight>();

    /**
     * 传入所需的单纯享元对象的内蕴状态，由工厂方法产生所需要的享元对象
     *
     * @param state 内蕴状态
     * @return
     */
    public Flyweight factory(Character state) {
        //先从缓存中查找对象
        Flyweight fly = files.get(state);
        if (fly == null) {
            //如果对象不存在则创建一个新的Flyweight对象
            fly = new ConcreteFlyweight(state);
            //把这个新的Flyweight对象添加到缓存中
            files.put(state, fly);
        }
        return fly;
    }
}