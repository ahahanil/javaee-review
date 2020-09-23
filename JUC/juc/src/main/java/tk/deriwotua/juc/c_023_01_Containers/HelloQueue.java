package tk.deriwotua.juc.c_023_01_Containers;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 容器
 *      Collection
 *          List
 *              CopyOnWriteList 写时复制容器
 *                  数组实现 并发安全
 *                  读写分离：读时共享、写时复制(原本的array)更新(且为独占式的加锁) JDK8使用的是ReentrantLock锁 JDK11使用的是synchronized同步代码块
 *                  添加元素时先加锁 然后数组元素拷贝到新数组(长度为旧数组+1) 最后新数组末尾元素赋值再替换掉旧数组
 *                  修改、删除都是写操作加锁
 *                  读取时存在弱一致性问题
 *                      所有的写操作都会产生新数组然后操作后替换旧数组
 *                      但是读操作都是读取旧数组，存在某个写线程变更了元素但新数组还未替换旧数组导致此时读的还是旧数据
 *              Vector
 *                  Stack
 *              ArrayList
 *                  非线程安全类变长的集合类，基于定长数组实现(O(1) 复杂度下完成随机查找操作)
 *                  允许空值和重复元素
 *                  添加时元素数量大于其底层数组容量时，其会通过扩容机制重新生成一个更大的数组
 *                      然后把旧数组数据拷贝到新数组里
 *                  指定 index 位置插入时会先校验下标是否合法然后判断是否需要扩容
 *                      然后把[index, size-index]区间元素一个一个拷贝方式向后移一位 O(N)
 *                      最后新元素插入至 index 处
 *                  删除元素是把该元素下标后面的元素一个一个拷贝向前移一位，最后把size值下标元素置null
 *
 *                  删除、插入时间复杂度都是O(n)
 *                  [参考](https://juejin.im/post/6844903904346374158)
 *                  ![JDK8-ArrayList添加元素](../assets/JDK8-ArrayList添加元素.png)
 *              LinkedList
 *          Set
 *              HashSet
 *                  LinkedHashSet
 *              SortedSet
 *                  TreeSet
 *              EnumSet
 *              CopyOnWriteArraySet
 *              ConcurrentSkipListSet
 *          Queue 接口
 *              提供非阻塞接口方法
 *                  boolean add(E e); 加入一个元素
 *                  boolean offer(E e); 队尾入队列
 *                  E remove();  删除一个元素
 *                  E poll(); 头节点出队列
 *                  E element(); 取队列顶部元素
 *                  E peek(); 检索头部元素，但不弹出
 *              AbstractQueue 抽象队列实现了 Queue接口
 *                   add(E e) 加入一个元素
 *                   remove() 删除一个元素
 *                   element() 取队列顶部元素调用peek 检索头部元素，但不弹出
 *                   clear() 反复调用poll删除所有元素
 *
 *              Deque   双端队列 继承 Queue 接口
 *                  既可以当作栈使用，也可以当作队列使用
 *                  可对容器的两端进行操作
 *
 *                  ArrayDeque 底层数组实现双端队列 继承AbstractCollection抽象方法实现了Deque双端队列接口
 *                      为了满足可以同时在数组两端插入或删除元素的需求，该数组还必须是循环的，即循环数组
 *                          数组的任何一点都可能被看作起点或者终点
 *                      循环数组所以 HEAD、TAIL指向是不固定的, 指向一致时会触发扩容
 *
 *                      效率比 Stack/LinkedList快
 *
 *                  BlockingDeque 双端阻塞队列 继承 BlockingQueue 阻塞队列接口
 *                      双端队列已满时，它将阻塞住试图插入元素的线程, 直到一个移除线程从该队列中移出了一个元素；
 *                      双端队列为空时，它将阻塞住试图抽取的线程, 直到一个插入线程向该队列插入了一个新元素。
 *
 *                      LinkedBlockingDeque 双向链表实现的双端并发阻塞队列 继承 AbstractQueue 抽象方法实现了BlockingDeque接口
 *                          同时支持FIFO和FILO两种操作方式
 *                          可重入锁和这个锁生成的两个条件对象进行并发控制
 *                          容量可选，不设置的话，就是Int的最大值
 *
 *              BlockingQueue 阻塞队列接口继承Queue接口
 *                  在Queue接口基础上
 *                      void put(E e); 生产者方法
 *                      E take();   消费者方法
 *
 *                  ArrayBlockingQueue 有界阻塞队列 继承AbstractQueue抽象方法实现了BlockingQueue阻塞队列接口
 *                      队列满后put()再添加则阻塞等待直到有空间插入
 *                              offer()再添加返回false 添加失败
 *                      队列空后take()再取则阻塞等待直到能取出数据
 *                              poll()返回null
 *
 *                  PriorityBlockingQueue 无界有序的阻塞队列 继承AbstractQueue抽象队列实现BlockingQueue接口
 *
 *                  LinkedBlockingQueue 无界阻塞队列 继承AbstractQueue抽象方法实现了BlockingQueue阻塞队列接口
 *                      实现了阻塞式put()、take()方法
 *                      队列满后put()再添加则阻塞等待直到有空间插入
 *                      队列空后take()再取则阻塞等待直到能取出数据
 *                      天然友好生产者&消费者模型
 *                          内部使用 ReentrantLock 加锁 (AQS)
 * 	                            底层基于 LockSupport#park()阻塞、LockSupport#unpark()唤醒、CAS操作
 *
 *                  TransferQueue 线程间任务传递阻塞队列接口 继承BlockingQueue阻塞队列接口
 *                      LinkedTransferQueue 线程间多任务传递阻塞队列 继承AbstractQueue抽象队列实现TransferQueue接口
 *                          ConcurrentLinkedQueue、SynchronousQueue（公平模式）和 LinkedBlockingQueue 的超集
 *                          生产者会一直阻塞直到所添加到队列的元素被某一个消费者所消费
 *
 *                  SynchronousQueue 无界、不存储元素的阻塞队列或栈(双栈双队列算法无空间的阻塞队列) 继承AbstractQueue抽象队列实现BlockingQueue接口
 *                      在某次put添加元素后必须等待其他线程take取走后才能继续添加反之亦然
 *                      在公平模式下
 *                      	采用公平锁，并配合一个FIFO双队列来阻塞多余的生产者和消费者，从而体系整体的公平策略
 *                      默认非公平模式
 *                      	采用非公平锁，同时配合一个LIFO双栈来管理多余的生产者和消费者 可能有某些生产者或者是消费者的数据永远都得不到处理
 *
 *                  DelayedWorkQueue 无界延迟阻塞队列 继承AbstractQueue抽象队列实现BlockingQueue接口
 *                      主要用于线程池定时或周期任务的使用
 *
 *              PriorityQueue 优先队列 基于二叉树排序队列 继承AbstractQueue抽象队列
 *                  PriorityQueue的底层数组实现小顶堆(任意一个非叶子节点的权值，都不大于其左右子节点的权值)
 *                  非线程安全
 *              ConcurrentLinkedQueue 继承AbstractQueue抽象队列实现Queue接口
 *                  基于CAS算法无锁线程安全 且 非阻塞 的 无界 队列
 *                  不允许存在null元素
 *                  offer(E o) 增加元素到队列尾部
 *                  poll() 取出头部元素，并弹出
 *                  peek() 检索头部元素，但不弹出
 *                  remove(Object o) 移除集合中的单个实例
 *              DelayQueue 时间上延迟排序队列(无界延时阻塞队列)  继承AbstractQueue抽象方法实现了BlockingQueue接口
 *                  基于 PriorityQueue 实现时间排序常用于按时间任务调度
 *                  队列中存放的元素必须是实现 java.util.concurrent.Delayed 接口
 * 	                    且必须有一个变量声明推迟多长时间
 * 	                延迟结合 PriorityQueue 优先队列
 * 	                    延迟时间作为权重
 * 	                    即延迟小的先出队列
 * 	                内部使用 ReentrantLock 加锁 (AQS)
 *                      底层基于 LockSupport#park()阻塞、LockSupport#unpark()唤醒、CAS操作
 *      Map
 *          HashMap
 *              LinkedHashMap
 *          TreeMap
 *          WeakHashMap
 *          IdentifyHashMap
 *          ConcurrentHashMap
 *          ConcurrentSkipListMap   有序数据存储 空间换时间
 *              基于 skip list跳表(跳跃列表)一种随机化的数据结构(上下左右都是链表的数据结构)
 *              节点主要由 Node, Index, HeadIndex 构成
 *              最下面那层链表是Node层(数据节点层), 上面几层都是Index层(索引)
 *
 *              ![跳表结构](../assets/跳表结构.png)
 *              ![跳表查找添加元素过程示意](../assets/跳表查找添加元素过程示意.png)
 * 为什么有List、Set后又搞了Queue
 *      Queue 针对高并发场景提供了一些线程友好的API
 *          offer peek poll
 *      BlockingQueue 又提供阻塞API
 *          put take 自然实现任务队列和生产者消费者模型
 */
public class HelloQueue {
    public static void main(String[] args) {
        Queue<Integer> q = new ArrayBlockingQueue<>(2);
        q.add(0);
        q.add(1);
        q.add(2);
        q.add(3);
        System.out.println(q);
    }
}
