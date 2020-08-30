package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

/**
 * Java 四种引用(指针指向)
 *  强引用(普通引用)
 *  软引用
 *      大对象的缓存
 *      常用对象的缓存
 *  弱引用
 *      缓存，没有容器引用指向时就需要清除的缓存
 *      ThreadLocal
 *      WeakHashMap
 *  虚引用
 *      管理堆外内存
 */
public class M {
    /**
     * 垃圾回收时会调用
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        // 垃圾回收时打印一句话便于观察
        System.out.println("我要被回收了");
    }
}
