package tk.deriwotua.dp.D03_factorymethod;

/**
 * 工厂模式
 *      简单工厂
 *      工厂方法
 *      抽象工厂
 *      Spring IOC
 */
public class Main {
    public static void main(String[] args) {
        Moveable m = new CarFactory().create();
        m.go();
    }
}
