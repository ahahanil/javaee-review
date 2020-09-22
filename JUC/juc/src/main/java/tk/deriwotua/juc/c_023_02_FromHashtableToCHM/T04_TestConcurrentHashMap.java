package tk.deriwotua.juc.c_023_02_FromHashtableToCHM;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 效率较高的线程安全Map
 *  利用了锁分段的思想提高了并发度
 *   JDK 1.6版本
 *      segment 继承了 ReentrantLock 充当锁的角色，为每一个 segment 提供了线程安全的保障；
 *      segment 维护了哈希散列表的若干个桶，每个桶由 HashEntry 构成的链表。
 *   JDK 1.8 的 ConcurrentHashMap 有了很大的变化，光是代码量就足足增加了很多。
 *      舍弃了 segment，并且大量使用了 synchronized，以及 CAS 无锁操作以保证 ConcurrentHashMap 操作的线程安全性
 *      不用 ReentrantLock 而是 synchronized 实际上，synchronized 做了很多的优化，包括偏向锁，轻量级锁，重量级锁
 *      相较于 ReentrantLock 的性能会持平甚至在某些情况更优
 *      底层数据结构改变为采用数组+链表+红黑树的数据形式
 *
 * ConcurrentHashMap 的关键属性(和HashMap类似)
 *      volatile Node<K,V>[] table:     装载 Node 的数组容器
 *          采用懒加载的方式，直到第一次插入数据的时候才会进行初始化操作，数组的大小总是为 2 的幂次方
 *      volatile Node<K,V>[] nextTable;     扩容时使用，平时为 null，只有在扩容的时候才为非 null
 *      volatile int sizeCtl;   控制 table 数组的大小
 *          当值为负数时：如果为-1 表示正在初始化，
 *              如果为-N 则表示当前正有 N-1 个线程进行扩容操作；
 *          当值为正数时：如果当前数组为 null 的话表示 table 在初始化过程中，sizeCtl 表示为需要新建数组的长度；
 *              若已经初始化了，表示当前数据容器（table 数组）可用容量也可以理解成临界值（插入节点数超过了该临界值就需要扩容），
 *              具体指为数组的长度 n 乘以 加载因子 loadFactor 即所能容纳的key-value对极限
 *          当值为 0 时，即数组长度为默认初始值。
 *      sun.misc.Unsafe U;     U.compareAndSwapXXXX 的方法去修改 ConcurrentHashMap 的一些属性
 *          CAS 操作 底层CMPXCHG指令实现
 *
 * 关键内部类
 *      Node<K,V> implements Map.Entry<K,V> {} 存放 key-value 对，并且具有 next 域 链表
 *      TreeNode<K,V> extends Node<K,V>{}  红黑树的操作是针对 TreeBin 类
 *      TreeBin<K,V> extends Node<K,V>{}   包装的很多 TreeNode 节点 红黑树结构中数组中，存放的是 TreeBin 对象，而不是 TreeNode 对象
 *      ForwardingNode<K,V> extends Node<K,V>{}     扩容时才会出现的特殊节点，其 key,value,hash 全部为 null。并拥有 nextTable 指针引用新的 table 数组
 *
 *  [参考](https://juejin.im/post/6844903602423595015)
 *
 *  ConcurrentHashMap 效率高体现在读上面插入数据时判断较多拖慢效率
 */
public class T04_TestConcurrentHashMap {

    static Map<UUID, UUID> m = new ConcurrentHashMap<>();

    static int count = Constants.COUNT;
    static UUID[] keys = new UUID[count];
    static UUID[] values = new UUID[count];
    static final int THREAD_COUNT = Constants.THREAD_COUNT;

    static {
        for (int i = 0; i < count; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    static class MyThread extends Thread {
        int start;
        int gap = count/THREAD_COUNT;

        public MyThread(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for(int i=start; i<start+gap; i++) {
                m.put(keys[i], values[i]);
            }
        }
    }

    public static void main(String[] args) {
        /**
         * 模拟一百万个键值对插入到ConcurrentHashMap里
         *  会发现 ConcurrentHashMap 最慢！？
         *  因为 ConcurrentHashMap 效率高体现在读上面插入数据时判断较多拖慢效率
         */
        long start = System.currentTimeMillis();

        Thread[] threads = new Thread[THREAD_COUNT];

        for(int i=0; i<threads.length; i++) {
            threads[i] =
            new MyThread(i * (count/THREAD_COUNT));
        }

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);

        System.out.println(m.size());

        //-----------------------------------
        /**
         * 模拟读取ConcurrentHashMap里一千万个键值对
         *  耗时不到 2s
         */
        start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 10000000; j++) {
                    m.get(keys[10]);
                }
            });
        }

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
