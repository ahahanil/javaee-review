package tk.deriwotua.dp.D04_abstractfactory.computer;

import tk.deriwotua.dp.D04_abstractfactory.computer.inter.IntelFactory;

/**
 * 电脑组装
 *  电脑配件CPU 主板等
 *  每家厂商都有自己的一套解决方案
 *      inter: inter主板、interCPU、inter芯片组...
 *      amd: amd主板、amdCPU、amd芯片组...
 *      ...
 *  采用工厂方法模式每家针对不同的产品都有类似的工厂来生产
 *      inter: inter主板工厂、interCPU工厂、inter芯片组工厂...
 *      amd: amd主板工厂、amdCPU工厂、amd芯片组工厂...
 *      ...
 *      使用工厂方法势必会导致随着配件越多工厂也越来越多
 *  是否可以使用同一个工厂来对付这些相同或者极为相似的配件呢？
 *      即同一个工厂负责多个不同配件产品对象的创建，这就是抽象工厂模式的好处。
 *      inter工厂: inter主板、interCPU、inter芯片组...
 *      amd工厂: amd主板、amdCPU、amd芯片组...
 *
 * 抽象工厂模式消费的一方不需要直接参与创建工作，而只需要向一个公用的工厂接口请求所需要资源。
 * 抽象工厂的功能是为一系列相关对象或相互依赖的对象创建一个接口
 *      接口内的方法不是任意堆砌的，而是一系列相关或相互依赖的方法
 *      比如上面例子中的主板和CPU，都是为了组装一台电脑的相关对象
 *
 * 抽象工厂模式的起源于创建分属于不同操作系统的视窗构建
 *
 * 抽象工厂模式的优点
 *     分离接口和实现
 * 　　   客户端使用抽象工厂来创建需要的对象，而客户端根本就不知道具体的实现是谁，客户端只是面向产品的接口编程而已。
 *        即客户端从具体的产品实现中解耦。
 *     使切换产品族变得容易
 * 　　   因为一个具体的工厂实现代表的是一个产品族，比如上面例子的从Intel系列到AMD系列只需要切换一下具体工厂。
 * 抽象工厂模式的缺点
 *     不太容易扩展新的产品
 *        如果需要给整个产品族添加一个新的产品，那么就需要修改抽象工厂，这样就会导致修改所有的工厂实现类。
 */
public class Client {
    public static void main(String[] args) {
        //创建装机工程师对象
        ComputerEngineer cf = new ComputerEngineer();
        //客户选择并创建需要使用的产品对象
        AbstractFactory af = new IntelFactory();
        //告诉装机工程师自己选择的产品，让装机工程师组装电脑
        cf.makeComputer(af);
    }
}