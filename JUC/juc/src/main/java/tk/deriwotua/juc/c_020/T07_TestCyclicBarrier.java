package tk.deriwotua.juc.c_020;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 同步屏障
 * 可以让一组线程达到一个屏障时被阻塞，直到最后一个线程达到屏障(组内所有线程都就位)时，所有被阻塞的线程才能继续执行
 * 而后栅栏将被重置以便下次使用
 * 基于 ReentrantLock 实现
 * 内也维护了 count 计数器每次调用java.util.concurrent.CyclicBarrier#await()会自减直到0
 */
public class T07_TestCyclicBarrier {
    public static void main(String[] args) {

        //CyclicBarrier barrier = new CyclicBarrier(20);
        /**
         * 设置屏障拦截的线程数量
         * 及到达屏障后执行Runnable任务
         */
        CyclicBarrier barrier = new CyclicBarrier(20, () -> System.out.println("满人"));

        /*CyclicBarrier barrier = new CyclicBarrier(20, new Runnable() {
            @Override
            public void run() {
                System.out.println("满人，发车");
            }
        });*/

        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                try {
                    /**
                     * 当前线程已经到达屏障位置(就位)，线程被阻塞，直到所有线程到达后唤醒
                     * 内部调用 java.util.concurrent.CyclicBarrier#dowait()
                     *  执行时先使用ReentrantLock#lock()加锁
                     *  缓存当前 Generation 为 g
                     *  然后java.util.concurrent.CyclicBarrier.Generation#broken判断栅栏是否损坏直接抛出异常
                     *      当一个线程处于等待状态另一个线程调用了重置屏障重置为初始状态时栅栏损坏或者出现异常或等待时被中断了
                     *  然后计数器自减在获取计数器值 为0 即最后一个线程就位
                     *      执行栅栏任务
                     *      唤醒之前等待的线程
                     *      count重置 generation重置
                     *      释放锁
                     *  否则如果没有时间限制，则直接等待，直到被唤醒
                     *  唤醒后继续执行
                     *      g != generation 表示正常换代了(generation重置了)，返回当前线程所在栅栏的下标
                     *     如果 g == generation，说明还没有换代，那为什么会醒了？
                     *     因为一个线程可以使用多个栅栏，当别的栅栏唤醒了这个线程，就会走到这里，所以需要判断是否是当前代。
                     *     正是因为这个原因，才需要generation来保证正确。
                     */
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
/**
 * CountDownLatch和CyclicBarrier的异同
 *   侧重点不同，CountDownLatch一般用于一个线程等待一组其它线程；而CyclicBarrier一般是一组线程间的相互等待至某同步点；
 *   CyclicBarrier的计数器是可以重置，而CountDownLatch不可以。
 *   CyclicBarrier能够获得更多线程状态信息
 */
