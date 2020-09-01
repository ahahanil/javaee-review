package tk.deriwotua.dp.D11_flyweight.multiflyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合享元模式
 *  单纯享元模式中，所有的享元对象都是单纯享元对象，也就是说都是可以直接共享的
 *  将一些单纯享元使用合成模式加以复合，形成复合享元对象。这样的复合享元对象本身不能共享，但是它们可以分解成单纯享元对象，而后者则可以共享
 */
public class Client {
    public static void main(String[] args) {
        List<Character> compositeState = new ArrayList<Character>();
        /**
         * 复合享元对象所含有的单纯享元对象的内蕴状态一般是不相等的
         *  内蕴状态 a、b、c
         */
        compositeState.add('a');
        compositeState.add('b');
        compositeState.add('c');
        compositeState.add('a');
        compositeState.add('b');

        FlyweightFactory flyFactory = new FlyweightFactory();
        Flyweight compositeFly1 = flyFactory.factory(compositeState);
        Flyweight compositeFly2 = flyFactory.factory(compositeState);
        /**
         * Composite Call 外蕴状态
         * 复合享元对象的所有单纯享元对象元素的外蕴状态都是与复合享元对象的外蕴状态相等的(都是 Composite Call)
         */
        compositeFly1.operation("Composite Call");

        System.out.println("---------------------------------");
        System.out.println("复合享元模式是否可以共享对象：" + (compositeFly1 == compositeFly2));

        Character state = 'a';
        Flyweight fly1 = flyFactory.factory(state);
        Flyweight fly2 = flyFactory.factory(state);

        System.out.println("单纯享元模式是否可以共享对象：" + (fly1 == fly2));
    }
}
