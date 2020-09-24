package tk.deriwotua.juc.c_020_juc_lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 是用来创建锁和其他同步类的基本线程阻塞原语
 * LockSupport 和 CAS 是Java并发包中很多并发工具控制机制的基础，它们底层其实都是依赖Unsafe实现。
 *
 * LockSupport 提供park()和 unpark()方法实现阻塞线程和解除线程阻塞
 * LockSupport和每个使用它的线程都与一个许可(permit)关联
 * permit相当于1，0的开关，默认是0，调用一次unpark就加1变成1，调用一次park会消费permit, 也就是将1变成0，同时park立即返回
 * 再次调用park会变成block（因为permit为0了，会阻塞在这里，直到permit变为1）, 这时调用unpark会把permit置为1。
 * 每个线程都有一个相关的permit, permit最多只有一个，重复调用unpark也不会积累
 */
public class T13_TestLockSupport {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                if(i == 5) {
                    /**
                     * 调用 park() 消费permit
                     *  permit 为0了，会阻塞在这里，直到permit变为1
                     * 循环里调用此方法某种意义上 park() 是“忙碌等待”的一种优化，它不会浪费这么多的时间进行自旋
                     */
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        /**
         * unpark() permit 加1
         *  permit 为1 时再调用unpark() 会把permit置为1 不会累计
         */
        LockSupport.unpark(t);

        /*try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after 8 senconds!");

        LockSupport.unpark(t);*/

    }
}
