package tk.deriwotua.juc.c_021_02_AQS;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阅读源码原则
 *  跑不起来不读
 *  解决问题就好（目的性） 现实中接手不知改了多少手的代码能解决问题就好忌通读
 *  一条线索到底
 *  无关细节略过
 *  一般不读静态
 *  一般动态读
 * 阅读源码就是理解他人思路
 *  数据结构
 *  设计模式
 *
 * Lock 实现
 *  底层 AQS(AbstractQueuedSynchronizer)队列同步器 + CAS + LockSupport
 *      AQS 底层 共享stat记录重入次数 + 双向链表节点存放里 Thread
 *          双向列表头节点 head 尾节点 tail
 *      核心就是通过 链表+CAS操作 锁定单个节点而替换锁定整个链表
 *          公平锁
 *              除了第一个线程外其它线程先入队列都阻塞等待LockSupport获取到许可
 *              而后释放锁时会通过LockSupport添加许可但仅仅是针对头节点进行唤醒队列其它节点都继续等待
 *
 *      LockSupport 控制节点线程许可(阻塞和唤醒)
 *
 *      节点 waitStatus 等待状态
 *          1 ，在队列中等待的线程等待超时或者被中断，从队列中取消等待；
 *          -1，后继节点处于等待；
 *          -2，节点在等待队列中，当condition被signal()后，会从等待队列转到同步队列；
 *          -3，表示下一次共享式同步状态获取将会被无条件传播下去；
 *          0 ，初始状态
 *    参考： https://www.cnblogs.com/nevermorewang/p/9848151.html
 */
public class TestReentrantLock {

    private static volatile int i = 0;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        /**
         * java.util.concurrent.locks.ReentrantLock#lock()调用java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(int)
         *      调用的是AQS方法参数为1
         * @see AbstractQueuedSynchronizer#acquire(int)
         *      尝试获取锁(以非公平锁为例)
         *     @see ReentrantLock.NonfairSync#tryAcquire(int)
         *          @see ReentrantLock.Sync#nonfairTryAcquire(int)
         *              获取 state 值 为 0 线程获取到锁 state CAS方式自增 记录线程为 exclusiveOwnerThread
         *                  非公平锁上来就尝试 CAS方式更新 state
         *                  而公平锁这里会 java.util.concurrent.locks.AbstractQueuedSynchronizer#hasQueuedPredecessors()
         *                      首先校验是否同步队列里是否存在节点不存在无等待线程可以CAS方式更新 state(即索取到锁)
         *                      否则校验头节点是否存在后继节点不存在可以CAS方式更新 state(即索取到锁)
         *                      否则(至少存在两个节点即至少两个线程在排队)
         *              不为0时若当前线程为 exclusiveOwnerThread已持有锁线程 state > 0 即锁重入 state 自增
         *                  否则未获取到锁
         *     未获取锁进入队列排队 首先创建节点添加到链表
         *     @see  AbstractQueuedSynchronizer#addWaiter(AbstractQueuedSynchronizer.Node)
         *          以共享锁还是排他锁模式添加 Waiter(节点node)
         *              正常链表插入节点做法就是加锁锁整个链表
         *                  所以存在锁范围太大问题
         *                  而这里基于CAS就只用锁tail这一个节点
         *              在tail尾节点后面通过CAS方式添加节点(Waiter)并把其置为tail节点
         *
         *              在JDK9中新API通过 java.lang.invoke.VarHandle 指针直接原子操作二进制码进行赋值来提高效率
         *     节点添加到队列后
         *     @see AbstractQueuedSynchronizer#acquireQueued(AbstractQueuedSynchronizer.Node, int)
         *          获取刚刚插入节点的前置节点
         *          如果前置节点是 head头节点 且 调用 tryAcquire(int) 再次尝试获取锁能获取到锁 即 前置节点(head头节点) 出队列然后重置head头节点
         *          如果没有获取到锁那么节点满足java.util.concurrent.locks.AbstractQueuedSynchronizer#shouldParkAfterFailedAcquire()
         *              条件后通过 java.util.concurrent.locks.AbstractQueuedSynchronizer#parkAndCheckInterrupt() 方法
         *              内部调用 LockSupport#park() 等待获取许可进入阻塞状态(等待前置节点 LockSupport#unpark() 添加许可后唤醒
         *          即新进节点插在队尾，然后其前置节点为头节点时尝试获取锁未获取到休眠等待头节点获取锁然后释放锁前唤醒直到成为头节点出队列
         *          (和Zookeeper锁类似监听前一个有序临时节点释放)
         */
        lock.lock();
        //synchronized (TestReentrantLock.class) {
            i++;
        //}

        /**
         * 内部调用java.util.concurrent.locks.AbstractQueuedSynchronizer#release(int)
         *  AQS 方法参数为1
         *  java.util.concurrent.locks.ReentrantLock.Sync#tryRelease(int)
         *      首先 state 减一
         *      校验当前线程是否为已获得锁的线程否则抛出异常
         *      当 state 为 0 时即没有锁重入
         *          缓存的已获取锁线程变量exclusiveOwnerThread置null
         *          返回true
         *          然后通过 java.util.concurrent.locks.AbstractQueuedSynchronizer#unparkSuccessor()
         *              内部 LockSupport#unpark() 唤醒队列头节点(占有锁节点)指向后继线程
         *      state 不为0 时 存在锁重入 返回false 不释放锁
         *
         */
        lock.unlock();

        //synchronized 程序员的丽春院 JUC
    }
}
