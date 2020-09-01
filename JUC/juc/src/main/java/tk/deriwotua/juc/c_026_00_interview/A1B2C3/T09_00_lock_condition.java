package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition本质是锁资源上不同的等待队列
 *  通过 Condition 可用精确唤醒某类线程而不是唤醒所有
 *      比如生产者/消费者模型中 可单独唤醒生产者生产
 */
public class T09_00_lock_condition {

    public static void main(String[] args) {

        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        Lock lock = new ReentrantLock();
        /**
         * t1 等待队列
         */
        Condition conditionT1 = lock.newCondition();
        /**
         * t2等待队列
         */
        Condition conditionT2 = lock.newCondition();

        new Thread(()->{
            try {
                lock.lock();

                for(char c : aI) {
                    System.out.print(c);
                    /**
                     * 只唤醒 t2 等待队列内的线程
                     */
                    conditionT2.signal();
                    conditionT1.await();
                }

                conditionT2.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }, "t1").start();

        new Thread(()->{
            try {
                lock.lock();

                for(char c : aC) {
                    System.out.print(c);
                    /**
                     * 只唤醒 t1 等待队列线程
                     */
                    conditionT1.signal();
                    conditionT2.await();
                }

                conditionT1.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }, "t2").start();
    }
}


