package tk.deriwotua.thread.communication.farmerchild;

/**
 * 消费者：从容器里取
 */
public class Child extends Thread {
    public void run() {
        /**
         * 不停取直到取完
         */
        while (true) {
            // 获取锁
            synchronized (Kuang.kuang) {
                /**
                 * 1.筐里没水果了就让小孩休息
                 */
                if (Kuang.kuang.size() == 0) {
                    try {
                        // 休眠 释放锁好让生产者获取锁去生产
                        Kuang.kuang.wait();
                    } catch (InterruptedException e) {
                    }
                }
                //2.小孩吃水果
                Kuang.kuang.remove("apple");
                System.out.println("小孩吃了一个水果,目前筐里有" + Kuang.kuang.size() + "个水果");
                //3.唤醒农夫继续放水果
                Kuang.kuang.notify();
            }   // 释放锁
            //4.模拟控制速度
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
