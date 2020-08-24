package tk.deriwotua.thread.communication.farmerchild;

/**
 * 生产者：往容器里添加
 */
public class Farmer extends Thread {
    public void run() {
        /**
         * 不停放直到放满
         */
        while (true) {
            // 获取锁
            synchronized (Kuang.kuang) {
                /**
                 * 1.筐放满了就让农夫休息
                 */
                if (Kuang.kuang.size() == 10) {
                    try {
                        // 容器放满了 休眠释放锁好让消费者获取锁去消费
                        Kuang.kuang.wait();
                    } catch (InterruptedException e) {
                    }
                }
                //2.往筐里放水果
                Kuang.kuang.add("apple");
                System.out.println("农夫放了一个水果,目前筐里有" + Kuang.kuang.size()
                        + "个水果");
                //3.容器放了就可以唤醒小孩继续吃
                Kuang.kuang.notify();
            }
            //4.模拟控制速度
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
