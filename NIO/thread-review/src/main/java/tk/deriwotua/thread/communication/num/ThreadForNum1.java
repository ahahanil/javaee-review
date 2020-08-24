package tk.deriwotua.thread.communication.num;

/**
 * 自定义一个线程类 输出1
 *  每次输出1后唤醒输出2的线程然后自己休眠
 */
public class ThreadForNum1 extends Thread {

    public void run(){
        for(int j=0;j<10;j++){
            /**
             * 加锁 此时就不能锁 this
             *      ThreadForNum2 和 ThreadForNum1是不同的线程类
             *      synchronized (this) 时线程间锁对象就不是同一把
             *      使用不同锁的线程之间不能相互唤醒，也就无法协调通信
             */
            synchronized (MyLock.o) {   //获取锁
                System.out.println(1);
                /**
                 * 线程间无法直接通信通过同一把锁对象协调通信
                 * 业务逻辑执行完后唤醒其它线程
                 * 同一个线程中要先唤醒其它线程，再让自己等待不能反
                 */
                MyLock.o.notify();
                try {
                    // 唤醒其它线程后自己休眠
                    MyLock.o.wait();    //释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
