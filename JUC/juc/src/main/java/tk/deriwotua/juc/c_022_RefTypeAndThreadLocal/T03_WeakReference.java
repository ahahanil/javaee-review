package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.lang.ref.WeakReference;
/**
 * 弱引用
 *  生命周期很短，不论当前内存是否充足，都只能存活到下一次垃圾收集之前
 *  通常用在容器中
 *      WeakHashMap
 *      ThreadLocal
 * 当一个强引用指向消失后就应该被回收
 *
 */
public class T03_WeakReference {
    public static void main(String[] args) {
        /**
         * 声明变量m指向一个弱引用对象(这里是强引用)
         * 而弱引用对象内部变量通过弱引用指向实例化对象M(这里的是弱引用)
         */
        WeakReference<M> m = new WeakReference<>(new M());

        System.out.println(m.get()); // 能获取到
        System.gc();    // 执行GC会回收弱引用对象
        System.out.println(m.get()); // null

        /**
         * 声明一个t1变量指向一个 ThreadLocal 容器
         */
        ThreadLocal<M> tl = new ThreadLocal<>();
        /**
         * ThreadLocal 容器里添加一个实例化对象M(指向实例化对象M)
         *  初始化了一个 ThreadLocalMap 在放在当前线程里
         *  而ThreadLocalMap的java.lang.ThreadLocal.ThreadLocalMap.Entry继承自WeakReference<ThreadLocal<?>>
         *  结论就是ThreadLocalMap.Entry的key是通过弱引用指向ThreadLocal容器
         *
         *  ![弱引用之ThreadLocal](assets/弱引用之ThreadLocal.png)
         */
        tl.set(new M());
        /**
         * 为了避免ThreadLocal被回收ThreadLocalMap.Entry的key值置为null导致整个value再也无法被访问
         * 而产生内存泄漏所以使用ThreadLocal对象后一定要remove()释放value
         * 图示参考 ![弱引用之ThreadLocal](assets/弱引用之ThreadLocal.png)
         */
        tl.remove();
    }
}

