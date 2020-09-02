package tk.deriwotua.dp.D21_TemplateMethod;

/**
 * 模板方法(钩子函数)
 *  所有重写的方法系统自动调用的都可以理解为模板方法
 */
public class Main {
    public static void main(String[] args) {
        F f = new C1();
        // 父类模板方法会调用子类重写方法
        f.m();
    }
}

/**
 * 抽象类
 */
abstract class F {
    /**
     * 模板方法明确调用了两个方法
     */
    public void m() {
        op1();
        op2();
    }

    /**
     * 抽象方法
     */
    abstract void op1();
    abstract void op2();
}

/**
 * 子类
 */
class C1 extends F {

    /**
     * 子类重写了父类方法
     */
    @Override
    void op1() {
        System.out.println("op1");
    }

    /**
     * 子类重写了父类方法
     */
    @Override
    void op2() {
        System.out.println("op2");
    }
}
