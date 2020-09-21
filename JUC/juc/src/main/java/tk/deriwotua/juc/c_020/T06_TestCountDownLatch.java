package tk.deriwotua.juc.c_020;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 允许一个或者多个线程一直等待，直到一组其它操作执行完成
 *  通过指定初始化计数器值count 通过调用 countDown() 方法递减，在当前计数到达零之前，await() 方法会一直受阻塞休眠
 *  递减为0时，CountDownLatch会唤醒所有调用await方法而休眠的线程
 *  声明定义CountDownLatch后计数器值无法进行重置
 *
 *  CountDownLatch 通过内部类 java.util.concurrent.CountDownLatch.Sync(继承AQS)
 *      这里的 count 最终传递赋值给了 java.util.concurrent.locks.AbstractQueuedSynchronizer#state
 *      AQS state在不同的实现类表示意义不同锁释放被占用、锁重入数、高低位标识读写锁计数、作为计数器值
 *
 *  这里可以实现和 Thread#join() 一样的效果
 */
public class T06_TestCountDownLatch {
    public static void main(String[] args) {
        usingJoin();
        usingCountDownLatch();
    }

    private static void usingCountDownLatch() {
        Thread[] threads = new Thread[100];
        /**
         * 倒计时器
         * 用给定的计数 初始化CountDownLatch
         */
        CountDownLatch latch = new CountDownLatch(threads.length);

        for(int i=0; i<threads.length; i++) {
            threads[i] = new Thread(()->{
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
                // 每个线程任务执行完 CountDownLatch实例化时指定数-1
                latch.countDown();
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        try {
            /**
             * CountDownLatch#await() 阻塞当前线程，并将当前线程封装加入到等待队列中
             * 阻塞直到初始化计数器值通过countDown()自减为0再往下继续执行
             *  可指定超时时间 超过超时时间后无论计数器是否为0都继续往下执行
             */
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end latch");
    }

    private static void usingJoin() {
        Thread[] threads = new Thread[100];

        for(int i=0; i<threads.length; i++) {
            threads[i] = new Thread(()->{
                int result = 0;
                for(int j=0; j<10000; j++) result += j;
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("end join");
    }
}
