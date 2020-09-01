package tk.deriwotua.dp.D01_singleton;

/**
 * 饿汉式
 * 在这个类被加载时，静态变量instance会被初始化，此时类的私有构造子会被调用，就实例化唯一实例，JVM保证线程安全
 * 饿汉式是典型的空间换时间，当类装载的时候就会创建类的实例，不管你用不用，先创建出来，然后每次调用的时候，就不需要再判断，节省了运行时间。
 *
 * 唯一缺点：不管用到与否，类装载时就完成实例化
 * Class.forName("")
 * （话说你不用的，你装载它干啥）
 */
public class Mgr01 {
    /**
     * 饿汉式其实是一种比较形象的称谓。既然饿，那么在创建对象实例的时候就比较着急，饿了嘛，于是在装载类的时候就创建对象实例。
     */
    private static final Mgr01 INSTANCE = new Mgr01();

    /**
     * 私有默认构造方法
     */
    private Mgr01() {
    };

    /**
     * 静态工厂方法
     */
    public static Mgr01 getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        Mgr01 m1 = Mgr01.getInstance();
        Mgr01 m2 = Mgr01.getInstance();
        System.out.println(m1 == m2);
    }
}
