package tk.deriwotua.juc.c_000;

/**
 * 线程状态切换
 *  ![线程状态](../assets/线程状态切换.jpeg)
 *
 * 1. 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
 * 2. 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。
 *      线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
 * 3. 阻塞(BLOCKED)：表示线程阻塞于锁。
 * 4. 等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
 * 5. 超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
 * 6. 终止(TERMINATED)：表示该线程已经执行完毕。
 */
public class T04_ThreadState {

    static class MyThread extends Thread {
        @Override
        public void run() {
            /**
             * 线程运行中 处于 RUNNABLE就绪状态
             *      细节上又分为 RUNNING运行中、READY就绪态等待系统调用
             *      RUNNING运行中线程调用 yield() 让出CPU时间片后进入 READY就绪态
             */
            System.out.println(this.getState());

            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        Thread t = new MyThread();
        // 未执行start() t 处于 new 初始状态
        System.out.println(t.getState());

        t.start();

        try {
            // 主线程 wait() 等待 t 线程执行完毕后唤醒
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * t 线程执行完毕 处于 TERMINATED 已终止状态
         */
        System.out.println(t.getState());
    }
}
