package tk.deriwotua.dp.D11_flyweight.simpleflyweight;

/**
 * 单纯的享元模式中，所有的享元对象都是可以共享的。
 *  工厂类首次创建所需的单纯享元对象时进行了缓存(共享)
 */
public class Client {
    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight fly = factory.factory(new Character('a'));
        fly.operation("First Call");

        fly = factory.factory(new Character('b'));
        fly.operation("Second Call");

        fly = factory.factory(new Character('a'));
        fly.operation("Third Call");
    }
}
