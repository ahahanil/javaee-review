package tk.deriwotua.juc.c_023_02_FromHashtableToCHM;

import java.util.HashMap;
import java.util.UUID;

/**
 * 数组：采用一段连续的存储单元来存储数据。对于指定下标的查找，时间复杂度为O(1)；
 *      通过给定值进行查找，需要遍历数组，逐一比对给定关键字和数组元素，时间复杂度为O(n)，
 *      当然，对于有序数组，则可采用二分查找，插值查找，斐波那契查找等方式，可将查找复杂度提高为O(logn)；
 *      对于一般的插入删除操作，涉及到数组元素的移动，其平均复杂度也为O(n)
 * 线性链表：对于链表的新增，删除等操作（在找到指定操作位置后），仅需处理结点间的引用即可，时间复杂度为O(1)，
 *      而查找操作需要遍历链表逐一进行比对，复杂度为O(n)
 * 二叉树：对一棵相对平衡的有序二叉树，对其进行插入，查找，删除等操作，平均复杂度均为O(logn)。
 * 哈希表：相比上述几种数据结构，在哈希表中进行添加，删除，查找等操作，性能十分之高，不考虑哈希冲突的情况下，仅需一次定位即可完成，时间复杂度为O(1)，
 *      哈希表是如何实现达到惊艳的常数阶O(1)的
 *
 *      哈希表的主干就是数组
 *      要新增或查找某个元素，通过把当前元素的关键字 通过某个哈希函数映射到数组中的某个位置，通过数组下标一次定位就可完成操作
 *          (Hash算法的后两步运算（高位运算和取模运算）来定位该键值对的存储位置，有时两个key会定位到相同的位置，表示发生了Hash碰撞)
 *          两个不同的元素，通过哈希函数得出的实际存储地址相同怎么办？
 *          即对某个元素进行哈希运算，得到一个存储地址，然后要进行插入的时候，发现已经被其他元素占用了，其实这就是所谓的哈希冲突，也叫哈希碰撞
 *          好的Hash算法计算结果越分散均匀，Hash碰撞的概率就越小，map的存取效率就会越高
 *          但是数组是一块连续的固定长度的内存空间，再好的哈希函数也不能保证得到的存储地址绝对不发生冲突
 *          开放定址法（发生冲突，继续寻找下一块未被占用的存储地址），再散列函数法，链地址法，而HashMap即是采用了链地址法，也就是数组+链表的方式
 *
 *          哈希桶数组很大，即使较差的Hash算法也会比较分散，如果哈希桶数组数组很小，即使好的Hash算法也会出现较多碰撞，
 *          所以就需要在空间成本和时间成本之间权衡，其实就是在根据实际情况确定哈希桶数组的大小，并在此基础上设计好的hash算法减少Hash碰撞
 *
 *          好的Hash算法和扩容机制就是为了保证尽量减少Hash碰撞
 * @see HashMap JDK1.7
 *      主干是一个Entry<K,V>[]数组  Entry是HashMap中的一个静态内部类
 *          transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
 *          每个Entry 存储指向下一个Entry的引用，单链表结构
 *          以及对key的hashcode值进行hash运算后得到的值，存储在Entry，避免重复计算
 *          ![HashMap整体结构](../assets/HashMap整体结构.png)
 *
 *      transient int size; 实际存储的key-value键值对的个数
 *      int threshold; 阈值(所能容纳的key-value对极限)，当table == {}时，该值为初始容量capacity（初始容量默认为16）；
 *          当table被填充了，也就是为table分配内存空间后，threshold 一般为 capacity*loadFactory
 *          threshold 就是在此 loadFactor 和capacity(数组长度)对应下允许的最大元素数目，超过这个数目就重新resize(扩容)，扩容后的HashMap容量是之前容量的两倍
 *      final float loadFactor; 负载因子，代表了table的填充度有多少，默认是0.75
 *          当加载因子设置比较大的时候，扩容的门槛就被提高了，扩容发生的频率比较低，占用的空间会比较小，但此时发生 Hash 冲突的几率就会提升，
 *              因此需要更复杂的数据结构来存储元素，这样对元素的操作时间就会增加，运行效率也会因此降低
 *          当加载因子值比较小的时候，扩容的门槛会比较低，因此会占用更多的空间，此时元素的存储就比较稀疏，发生哈希冲突的可能性就比较小，因此操作性能会比较高
 *
 *          0.75是对空间和时间效率的一个平衡选择
 *          在数组定义好长度之后，负载因子越大，所能容纳的键值对个数越多
 *          如果内存空间很多而又对时间效率要求很高，可以降低负载因子 loadFactor 的值
 *          相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子loadFactor的值，这个值可以大于1
 *      transient int modCount; 用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，
 *          如果期间其他线程的参与导致HashMap的结构发生变化了（比如put新键值对(值被覆盖不属于)，remove等操作），
 *          需要抛出异常ConcurrentModificationException
 *
 *  HashMap 构造方法
 *      没有传入 initialCapacity 和 loadFactor 这两个参数，会使用默认值 initialCapacity默认为16，loadFactory默认为0.75
 *      常规构造方法中，没有为数组table分配内存空间（有一个入参为指定Map的构造方法例外），而是在执行put操作的时候才真正构建table数组
 *
 * HashMap#put(key, value) 方法
 *      首先如果table数组为空数组{}，HashMap#inflateTable(threshold)进行数组填充（为table分配实际内存空间）
 *          入参为 threshold，此时 threshold 为 initialCapacity 默认是 1<<4(2^4=16)
 *          HashMap#inflateTable(threshold)首先确保 capacity 为大于或等于toSize(传入的threshold)的最接近toSize的二次幂
 *          然后创建 Entry[capacity] 大小数组
 *      如果key为null，存储位置为table[0]或table[0]的冲突链上
 *          JDK8 计算数组索引下标，如果索引下标对应table[index]为null，直接插入
 *          JDK8 不为null 时 是红黑树节点就插入红黑树；如果是链表，就使用尾插法插入链表末尾
 *      通过HashMap#hash(key) 对key的hashcode进一步计算，尽量确保散列均匀(最终获取的存储位置尽量分布均匀)得到 hash值
 *      HashMap#indexFor(hash, table.length) 获取在table中的实际位置 index
 *          HashMap#hash(key)、HashMap#indexFor()做hash运算：取key的hashCode值、高位运算、取模运算
 *
 *          ![JDK8-HashMap的hash运算](../assets/JDK8-HashMap的hash运算.jpg)
 *      取出数组中该下标位置的元素 Entry<key, value> 存在且 key一致则进行更新操作
 *          如果key值不一致则循环通过Entry#next比对链表上下一个非null节点key是否一样一样更新返回
 *      如果 index 下标的链表上没有一个节点与 key 一致 那么
 *          首先 modCount自增 保证并发访问时，若HashMap内部结构发生变化，快速响应失败
 *          然后通过HashMap#addEntry() 新增一个entry
 *              新增前先判断 实际存储个数size是否超过临界阈值threshold，即将发生哈希冲突时进行扩容
 *              当发生哈希冲突并且size大于阈值的时候，需要进行数组扩容，扩容时，需要新建一个长度为之前数组2倍的新的数组，
 *              然后将当前的Entry数组中的元素HashMap#transfer() 全部传输过去，扩容后的新数组长度为之前的2倍，所以扩容相对来说是个耗资源的操作。
 *
 *              HashMap#resize(newCapacity) 根据传入新容量数组进行扩容，数组长度发生变化，即存储位置index也可能发生变化需要重新计算index
 *                  初始化一个新的Entry数组
 *                  在 HashMap#transfer() 传输时 for循环中，逐个遍历链表，重新计算索引位置，
 *                      将老数组数据复制到新数组中去（数组不存储实际数据，所以仅仅是拷贝引用而已）
 *                          单链表的头插法插入方式(多线程下容易生产循环引用)
 *                          同一位置上新元素总会被放在链表的头部位置；这样先放在一个索引上的元素终会被放到Entry链的尾部和 JDK8 尾部正序插入 有区别
 *                      扔到新的扩容后的数组中数组索引位置的计算是通过 对key值的hashcode进行hash扰乱运算后，再通过和 length-1进行位运算得到最终数组索引位置。
 *                      （JDK 1.8 在扩容时并没有像 JDK 1.7 那样，重新计算每个元素的哈希值，而是通过高位运算（e.hash & oldCap）来确定元素是否需要移动）
 *                      HashMap的数组长度一定是2的次幂可以减少数据位置重新调换次数
 *                  HashMap的table指向新的Entry数组
 *                  根据新容量更新阈值
 *  HashMap#get(key)
 *      如果key为null,则直接去table[0]处去检索即可
 *      JDK8根据key计算对应数组索引位置，先比较数组元素，匹配的话直接返回；不匹配，遍历其树结构或链表，找到后返回该节点，找不到返回null
 *      非 null HashMap#getEntry(key)
 *          通过key的hashcode值计算hash值 通过HashMap#indexFor()获取最终数组索引，
 *          如果索引出存放的是链表然后遍历链表，通过equals方法比对找出对应记录
 * 重写equals方法需同时重写hashCode方法
 *      get和put操作的时 使用的key从逻辑上讲是等值的（通过equals比较是相等的）
 *      但由于没有重写hashCode方法，所以put操作时，key(hashcode1)-->hash-->indexFor-->最终索引位置，
 *      而通过key取出value的时候 key(hashcode1)-->hash-->indexFor-->最终索引位置，由于hashcode1不等于hashcode2，
 *      导致没有定位到一个数组位置而返回逻辑上错误的值null（也有可能碰巧定位到一个数组位置，但是也会判断其entry的hash值是否相等）
 *
 * 上面是基于 JDK7 中HashMap实现
 * 在 JDK8 中HashMap是 数组+链表+红黑树（JDK1.8增加了红黑树部分）实现
 *      ![JDK8-HashMap是数组-链表-红黑树实现](../assets/JDK8-HashMap是数组-链表-红黑树实现.png)
 *
 *      Node[] table，即哈希桶数组，明显它是一个Node的数组
 *      static class Node<K,V> implements Map.Entry<K,V> { // HashMap的一个内部类，实现了Map.Entry接口，本质是就是一个映射(键值对)
 *          final int hash; //用来定位数组索引位置
 *          final K key;
 *          V value;
 *          Node<K,V> next; //链表的下一个node
 *          Node(int hash, K key, V value, Node<K,V> next) { ... }
 *          public final K getKey(){ ... }
 *          public final V getValue() { ... }
 *          public final String toString() { ... }
 *          public final int hashCode() { ... }
 *          public final V setValue(V newValue) { ... }
 *          public final boolean equals(Object o) { ... }
 *      }
 *
 *      即使负载因子和Hash算法设计的再合理，也免不了会出现拉链过长的情况，一旦出现拉链过长，则会严重影响HashMap的性能。
 *      于是，在JDK1.8版本中，对数据结构做了进一步的优化，引入了红黑树。
 *      而当链表长度太长（默认超过8）时，链表就转换为红黑树，利用红黑树快速增删改查的特点提高HashMap的性能，其中会用到红黑树的插入、删除、查找等算法
 *
 *  ![JDK8-HashMap的put流程](../assets/JDK8-HashMap的put流程.jpg)
 *
 * HashMap中，如果key经过hash算法得出的数组索引位置全部不相同，即Hash算法非常好，那样的话，getKey方法的时间复杂度就是O(1)，
 * 如果Hash算法技术的结果碰撞非常多，假如Hash算极其差，所有的Hash算法结果得出的索引位置一样，
 * 那样所有的键值对都集中到一个桶中，或者在一个链表中，或者在一个红黑树中，时间复杂度分别为O(n)和O(lgn)。
 * 鉴于JDK1.8做了多方面的优化，总体性能优于JDK1.7
 *      Hash算法较均匀，JDK1.8引入的红黑树效果不明显
 *
 * 扩容是一个特别耗性能的操作，所以在使用HashMap的时候，估算map的大小，初始化的时候给一个大致的数值，避免map进行频繁的扩容。
 * 负载因子是可以修改的，也可以大于1，但是建议不要轻易修改，除非情况非常特殊。
 * HashMap是线程不安全的，不要在并发的环境中同时操作HashMap，建议使用ConcurrentHashMap。
 * JDK1.8引入红黑树大程度优化了HashMap的性能。
 * HashMap的性能提升仅仅是JDK1.8的冰山一角
 *
 * HashMap为什么会发生死循环
 *      JDK7 HashMap#transfer()为例 多线程下添加元素存在某个线程 rehash 后链表的顺序被反转线程间不可见就会导致循环引用
 *      死循环的原因是 JDK 1.7 链表插入方式为倒序头插入，这个问题在 JDK 1.8 得到了改善，变成了尾部正序插入
 * 很多情况下是不需要考虑线程安全问题 Hashtable就不合时宜了
 *  HashMap 全不加锁
 */
public class T02_TestHashMap {

    static HashMap<UUID, UUID> m = new HashMap<>();

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

    /**
     * 模拟一百万个键值对插入到HashMap里
     *  HashMap 线程不安全的所以最终放到HashMap键值对是小于一百万个
     * @param args
     */
    public static void main(String[] args) {

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
    }
}
