package tk.deriwotua.dp.D01_singleton;

/**
 * 之前单例实现方式都存在小小的缺陷，那么有没有一种方案，既能实现延迟加载，又能实现线程安全呢？
 *  Lazy initialization holder class模式
 *
 * Lazy initialization holder class模式综合使用了Java的类级内部类和多线程缺省同步锁的知识，很巧妙地同时实现了延迟加载和线程安全。
 *  类级内部类指的是有static修饰的成员式内部类
 *      如果没有static修饰的成员式内部类被称为对象级内部类。
 *  类级内部类相当于其外部类的成员，只有在第一次被使用的时候才被会装载。
 *
 *  多线程缺省同步锁(在某些情况中，JVM已经隐含地执行了同步，这些情况下就不用自己再来进行同步控制了)
 *  　 1.由静态初始化器（在静态字段上或static{}块中的初始化器）初始化数据时
 * 　　2.访问final字段时
 * 　　3.在创建线程之前创建对象时
 * 　　4.线程可以看见它将要处理的对象时
 *
 * 思路(静态内部类可分类级内部类、对象级内部类)
 *  类级内部类(静态内部类JVM隐含地同步)里面去创建对象实例。这样一来，只要不使用到这个类级内部类，那就不会创建对象实例，从而同时实现延迟加载和线程安全。
 *  加载外部类时不会加载内部类，这样可以实现懒加载
 */
public class Mgr07 {

    private Mgr07() {
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，
     * 该内部类的实例与外部类的实例没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class Mgr07Holder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private final static Mgr07 INSTANCE = new Mgr07();
    }

    /**
     * 当getInstance方法第一次被调用的时候，它第一次读取Mgr07Holder.INSTANCE，导致Mgr07Holder类得到初始化；
     * 而这个类在装载并被初始化的时候，会初始化它的静态域，从而创建Mgr07的实例，
     * 由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
     * @return
     */
    public static Mgr07 getInstance() {
        return Mgr07Holder.INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr07.getInstance().hashCode());
            }).start();
        }
    }


}
