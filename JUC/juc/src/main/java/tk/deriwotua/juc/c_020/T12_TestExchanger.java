package tk.deriwotua.juc.c_020;

import java.util.concurrent.Exchanger;

/**
 * Exchanger 可以在两个线程之间配对和交换数据的线程的同步点，只能是2个线程，不支持更多的线程之间互换数据
 * 允许在并发任务之间交换数据。具体来说，Exchanger类允许在两个线程之间定义同步点
 * 当两个线程都到达同步点时，他们交换数据结构，因此第一个线程的数据结构进入到第二个线程中，第二个线程的数据结构进入到第一个线程中。
 */
public class T12_TestExchanger {

    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            String s = "T1";
            try {
                /**
                 * 一个线程已经到达了exchanger节点时，对于它的伙伴线程的情况有三种
                 *   如果它的伙伴线程在该线程到达之前已经调用了exchanger方法，则它会唤醒它的伙伴然后进行数据交换，得到各自数据返回。
                 *   如果它的伙伴线程还没有到达交换点，则该线程将会被挂起，等待它的伙伴节点到达被唤醒，完成数据交换。
                 *   如果当前线程被中断了则抛出异常，或者等待超时了，则抛出超时异常
                 */
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);

        }, "t1").start();


        new Thread(() -> {
            String s = "T2";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);

        }, "t2").start();

    }
}
