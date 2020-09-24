package tk.deriwotua.juc.c_020_juc_lock;

import java.util.concurrent.Semaphore;

/**
 * Semaphore (计数)信号量
 * 控制同时访问某个特定资源的操作数量，或者同时执行某个指定操作的数量。
 * 信号量还可以用来实现某种资源池，或者对容器施加边界。
 *
 * Semaphore管理着一组许可（permit）,许可的初始数量可以通过构造函数设定，操作时首先要获取到许可，才能进行操作，
 * 操作完成后需要释放许可。如果没有获取许可，则阻塞到有许可被释放。
 * 如果初始化了一个许可为1的Semaphore，那么就相当于一个不可重入的互斥锁（Mutex）
 *
 * synchronized 的加强版，作用是控制线程的并发数量(控制某个资源可被同时访问的个数)
 *
 * 内部类 java.util.concurrent.Semaphore.Sync 实现AQS方法在此基础上实现了公平锁和非公平锁
 */
public class T11_TestSemaphore {
    public static void main(String[] args) {
        //Semaphore s = new Semaphore(2);
        /**
         * 创建具有给定的许可数和给定的公平设置的Semaphore
         * 限制同一个时刻，只允许有2个线程执行任务
         * 初始化了 2个通路(permits许可)
         */
        Semaphore s = new Semaphore(2, true);
        //允许一个线程同时执行
        //Semaphore s = new Semaphore(1);

        new Thread(()->{
            try {
                /**
                 * Semaphore#acquire() 获取一个许可
                 * 在 semaphore.acquire() 和 semaphore.release()之间的代码，同一时刻只允许指定个数的线程进入，
                 * 因为semaphore的构造方法是2，则同一时刻只允许2个线程进入，其他线程只能等待。
                 *
                 * 同时访问的任务数已满(达到指定最大许可数)，则其他 acquire 的任务进入等待状态，直到有一个任务被 release 掉，它才能得到许可
                 */
                s.acquire();
                /**
                 * java.util.concurrent.Semaphore#acquire(int)也可以使用带参的acquire(permits)
                 * 表示当前线程需要占用几个许可
                 */

                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                /**
                 * 释放一个许可
                 */
                s.release();
            }
        }).start();

        new Thread(()->{
            try {
                s.acquire();

                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");

                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
