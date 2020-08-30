/**
 * 软引用
 * 软引用是用来描述一些还有用但并非必须的对象。
 * 对于软引用关联着的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围进行第二次回收。
 * 如果这次回收还没有足够的内存，才会抛出内存溢出异常。
 * -Xmx20M
 */
package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.lang.ref.SoftReference;

/**
 * 软引用
 *  当某个变量采用软引用方式指向某块空间时只有当内存不够用时才会回收这快空间
 *  执行时配置 JVM参数
 *      -Xms20M -Xmx20M 把初始化堆和最大堆空间设置小点便于观察
 *  先通过软引用的方式申请10M空间
 *  只要内存占用不超过20M上面空间就不会被清理
 *  当再次申请15M空间时JVM发现空闲只有5M不到，先执行GC一次发现空间还是不够申请所需15M
 *  此时就会清理掉软引用10M空间
 * 软引用非常适合缓存使用
 *  从数据库中load的一些数据就可以通过软引用缓存到内存里
 */
public class T02_SoftReference {
    public static void main(String[] args) {
        /**
         * 声明一个变量m指向一个软引用对象(这里是强引用)
         * 软引用对象内部有一个变量指向一块10M的空间(这里是软引用)
         */
        SoftReference<byte[]> m = new SoftReference<>(new byte[1024*1024*10]);
        //m = null;
        // 获取软引用对象的空间数据
        System.out.println(m.get());
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m.get());

        //再分配一个15M数组，heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
        byte[] b = new byte[1024*1024*15];
        System.out.println(m.get());
    }
}
