/**
 *
 *
 *     一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，
 *     也无法通过虚引用来获取一个对象的实例。
 *     为一个对象设置虚引用关联的唯一目的就是能在这个对象被收集器回收时收到一个系统通知。
 *     虚引用和弱引用对关联对象的回收都不会产生影响，如果只有虚引用活着弱引用关联着对象，
 *     那么这个对象就会被回收。它们的不同之处在于弱引用的get方法，虚引用的get方法始终返回null,
 *     弱引用可以使用ReferenceQueue,虚引用必须配合ReferenceQueue使用。
 *
 *     jdk中直接内存的回收就用到虚引用，由于jvm自动内存管理的范围是堆内存，
 *     而直接内存是在堆内存之外（其实是内存映射文件，自行去理解虚拟内存空间的相关概念），
 *     所以直接内存的分配和回收都是有Unsafe类去操作，java在申请一块直接内存之后，
 *     会在堆内存分配一个对象保存这个堆外内存的引用，
 *     这个对象被垃圾收集器管理，一旦这个对象被回收，
 *     相应的用户线程会收到通知并对直接内存进行清理工作。
 *
 *     事实上，虚引用有一个很重要的用途就是用来做堆外内存的释放，
 *     DirectByteBuffer就是通过虚引用来实现堆外内存的释放的。
 *
 */


package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;

/**
 * 虚引用
 *  不会影响对象的生命周期，所持有的引用就跟没持有一样，随时都能被GC回收
 *  必须和引用队列关联使用
 *  在对象的垃圾回收过程中，如果GC发现一个对象还存在虚引用，则会把这个虚引用加入到与之关联的引用队列中。
 *  程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。
 *  如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象内存被回收之前采取必要的行动防止被回收。
 *  JVM开发中管理堆外内存（主要用来跟踪对象被垃圾回收器回收的活动）
 */
public class T04_PhantomReference {
    private static final List<Object> LIST = new LinkedList<>();
    /**
     * 引用队列
     */
    private static final ReferenceQueue<M> QUEUE = new ReferenceQueue<>();

    public static void main(String[] args) {
        /**
         * 虚引用必须指定队列
         * 声明phantomReference指向虚引用对象(强引用)
         * 虚引用对象内部变量通过虚引用指向实例化对象M
         * 虚引用对象内部变量通过虚引用指向引用队列
         *  当虚引用指向实例化对象M被回收时放入引用队列产生一个通知
         */
        PhantomReference<M> phantomReference = new PhantomReference<>(new M(), QUEUE);

        /**
         * 不断申请空间
         */
        new Thread(() -> {
            while (true) {
                LIST.add(new byte[1024 * 1024]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                /**
                 * 虚引用永远都无法通过API拿到值
                 */
                System.out.println(phantomReference.get());
            }
        }).start();

        new Thread(() -> {
            while (true) {
                /**
                 * 不断监测队列里是否存在值即虚引用是否入队列了
                 */
                Reference<? extends M> poll = QUEUE.poll();
                if (poll != null) {
                    System.out.println("--- 虚引用对象被jvm回收了 ---- " + poll);
                }
            }
        }).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

