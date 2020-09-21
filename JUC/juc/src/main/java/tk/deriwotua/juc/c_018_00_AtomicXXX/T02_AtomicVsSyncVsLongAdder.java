package tk.deriwotua.juc.c_018_00_AtomicXXX;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * java.util.concurrency.atomic.LongAdder是Java8新增的一个类，提供了原子累计值的方法。根据文档的描述其性能要优于AtomicLong
 * AtomicLong 从Java8开始针对x86平台进行了优化，使用XADD替换了CAS操作
 *
 * 大部分情况下,CAS都能够提供不错的性能，但是在高竞争的情况下自旋开销可能会成倍增长
 *
 * Unsafe#getAndAddLong()方法会以volatile的语义去读需要自增的域的最新值，然后通过CAS去尝试更新，正常情况下会直接成功后返回，
 * 但是在高并发下可能会同时有很多线程同时尝试这个过程，也就是说线程A读到的最新值可能实际已经过期了，
 * 因此需要在while循环中不断的重试，造成很多不必要的开销，
 * 而xadd的相对来说会更高效一点，最重要的是源码上是原子的,也就是说其他线程不能打断它的执行或者看到中间值
 *
 * 而LongAdder的性能比上面那种还要好很多，首先它有一个基础的值base，在发生竞争的情况下，
 * 会有一个Cell数组用于将不同线程的操作离散到不同的节点上去(会根据需要扩容，最大为CPU核数)，
 * sum()会将所有Cell数组中的value和base累加作为返回值。核心的思想就是将AtomicLong一个value的更新压力分散到多个value中去，从而降级更新热点。
 *
 * LongAdder继承自Striped64，Striped64内部维护了一个懒加载的数组以及一个额外的base实力域，数组的大小是2的N次方，使用每个线程Thread内部的哈希值访问。
 * 数组的元素是 @jdk.internal.vm.annotation.Contended static final class Cell{} 类，可以看到Cell类用Contended注解修饰，这里主要是解决false sharing
 * (错误的共享)
 *  比如两个volatile变量被分配到了同一个缓存行，但是这两个的更新在高并发下会竞争，比如线程A去更新变量a，线程B去更新变量b，
 *  但是这两个变量被分配到了同一个缓存行，因此会造成每个线程都去争抢缓存行的所有权，
 *  例如A获取了所有权然后执行更新这时由于volatile的语义会造成其刷新到主存，但是由于变量b也被缓存到同一个缓存行，
 *  因此就会造成cache miss，这样就会造成极大的性能损失，因此有一些类库的作者，例如JUC下面的、Disruptor等都利用了插入dummy 变量的方式，使得缓存行被其独占
 *  Contended注解以防cells数组发生伪共享的情况
 *  Cell相对来说比较占内存，因此这里采用懒加载的方式，在无竞争的情况下直接更新base域，在第一次发生竞争的时候(CAS失败)就会创建一个大小为2的cells数组，每次扩容都是加倍，只到达到CPU核数
 *  扩容数组等行为需要只能有一个线程同时执行，因此需要一个锁
 */
public class T02_AtomicVsSyncVsLongAdder {
    static long count2 = 0L;
    static AtomicLong count1 = new AtomicLong(0L);
    static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[1000];

        for(int i=0; i<threads.length; i++) {
            threads[i] =
                    new Thread(()-> {
                        for(int k=0; k<100000; k++) count1.incrementAndGet();
                    });
        }

        long start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        long end = System.currentTimeMillis();

        //TimeUnit.SECONDS.sleep(10);

        System.out.println("Atomic: " + count1.get() + " time " + (end-start));
        //-----------------------------------------------------------
        Object lock = new Object();

        for(int i=0; i<threads.length; i++) {
            threads[i] =
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 相比 synchronized移到循环内部锁细化 这里的效率更高
                         * 大概相差 10 倍 count2自增本身不耗时而这里放在循环内存在频繁的获取锁释放锁动作本身就是开销
                         * synchronized 无锁 -> 偏向锁 -> 轻量锁 -> 重量级锁
                         * 这里锁升级至 轻量级锁（CAS + 自旋）按道理讲效率上不会和其它相差太多
                         */
                        synchronized (lock) {
                            for (int k = 0; k < 100000; k++)
                                //synchronized (lock) {
                                    count2++;
                                //}
                        }
                    }
                });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        end = System.currentTimeMillis();


        System.out.println("Sync: " + count2 + " time " + (end-start));


        //----------------------------------
        for(int i=0; i<threads.length; i++) {
            threads[i] =
                    new Thread(()-> {
                        for(int k=0; k<100000; k++) count3.increment();
                    });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        end = System.currentTimeMillis();

        //TimeUnit.SECONDS.sleep(10);

        System.out.println("LongAdder: " + count1.longValue() + " time " + (end-start));

    }

    static void microSleep(int m) {
        try {
            TimeUnit.MICROSECONDS.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
