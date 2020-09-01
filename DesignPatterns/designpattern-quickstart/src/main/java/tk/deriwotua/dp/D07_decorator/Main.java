package tk.deriwotua.dp.D07_decorator;

import java.io.*;

/**
 * Decorator Pattern 装饰模式又名包装(Wrapper)模式
 *  ![装饰器模式的UML类图](../assets/装饰器模式的UML类图.png)
 *  按照单一职责原则，某一个对象只专注于干一件事，而如果要扩展其职能的话，不如想办法分离出一个类来“包装”这个对象，而这个扩展出的类则专注于实现扩展功能。
 * 装饰器模式就可以将新功能动态地附加于现有对象而不改变现有对象的功能。客户端并不会觉得对象在装饰前和装饰后有什么不同。装饰模式可以在不使用创造更多子类的情况下，将对象的功能加以扩展。
 * 动态地给一个对象添加一些额外的职责，就增加功能来说，装饰器模式比生成子类更灵活。
 * Java提供的工具包中，IO相关工具就普遍大量使用了装饰器模式，例如充当装饰功能的IO类如BufferedInputStream等，又被称为高级流，通常将基本流作为高级流构造器的参数传入，将其作为高级流的一个关联对象，从而对其功能进行扩展和装饰。
 *
 * 从类图上看，装饰器模式与代理模式很像，是它们的目的不同，所以使用方法和适用场景上也就不同 ，装饰器模式与代理模式的区别：
 *     代理模式专注于对被代理对象的访问；
 *     装饰器模式专注于对被装饰对象附加额外功能。
 * 适用场景
 *      运行时，需要动态地为对象增加额外职责时；
 *      当需要一个能够代替子类的类，借助它提供额外方法时。
 *
 * coffee包下使用这一模式模拟假设去买咖啡，首先服务员给冲了一杯原味咖啡，我希望服务员给加些牛奶和白糖混合入原味咖啡中
 */
public class Main {
    public static void main(String[] args) throws Exception {
        File f = new File("c:/work/test.data");
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write("http://www.mashibing.com");
        bw.flush();
        bw.close();
    }
}
