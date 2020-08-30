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
 *  底层 AQS(AbstractQueuedSynchronizer) + CAS
 *      AQS 底层 共享stat记录重入次数 + 双向链表节点存放里 Thread
 *          双向列表头节点 head 尾节点 tail
 *      核心就是通过 链表+CAS操作 锁定单个节点而替换锁定整个链表
 */
public class TestReentrantLock {

    private static volatile int i = 0;

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        /**
         * @see AbstractQueuedSynchronizer#acquire(int)
         *      尝试获取锁
         *     @see ReentrantLock.NonfairSync#tryAcquire(int)
         *          @see ReentrantLock.Sync#nonfairTryAcquire(int)
         *              获取 stat 值 为 0 线程获取到锁
         *              为 > 0 即重入 stat CAS方式自增
         *     未获取锁进入队列排队 首先创建节点添加到链表
         *     @see  AbstractQueuedSynchronizer#addWaiter(AbstractQueuedSynchronizer.Node)
         *          以共享锁还是排他锁模式添加 Waiter(节点node)
         *              正常链表插入节点做法就是加锁锁整个链表
         *                  所以存在锁范围太大问题限制基于CAS就只用锁tail这一个节点
         *              在tail尾节点后面通过CAS方式添加节点(Waiter)并把其置为tail节点
         *
         *              在JDK9中新API通过 VarHandle(指针)直接原子操作二进制码进行赋值来提高效率
         *     节点添加到队列后
         *     @see AbstractQueuedSynchronizer#acquireQueued(AbstractQueuedSynchronizer.Node, int)
         *          获取刚刚插入节点的前置节点
         *          如果前置节点是 head头节点 且 调用 tryAcquire(int) 能获取到锁 即 前置节点(head头节点) 出队列然后重置head头节点
         *          如果没有获取到锁那么节点进入阻塞状态等待前置节点释放锁前的唤醒
         *          即新进节点插在队尾，然后其前置节点为头节点时尝试获取锁未获取到休眠等待头节点获取锁然后释放锁前唤醒直到成为头节点出队列
         *          (和Zookeeper锁类似监听前一个有序临时节点释放)
         */
        lock.lock();
        //synchronized (TestReentrantLock.class) {
            i++;
        //}

        lock.unlock();

        //synchronized 程序员的丽春院 JUC
    }
}
