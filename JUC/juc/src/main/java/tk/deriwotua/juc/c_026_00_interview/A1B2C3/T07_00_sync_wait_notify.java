package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

/**
 * 如果想保证t2在t1之前打印，也就是说保证首先输出的是A而不是1，这个时候该如何做？
 */
public class T07_00_sync_wait_notify {

    private static volatile boolean t2Started = false;

    /**
     * CountDownLatch 也可实现
     */
    //private static CountDownLatch latch = new C(1);

    public static void main(String[] args) {
        final Object o = new Object();

        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(()->{
            //latch.await();
            synchronized (o) {
                /**
                 * t2如果还未执行先等待其先执行
                 */
                while(!t2Started) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(char c : aI) {
                    System.out.print(c);
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                o.notify();
            }
        }, "t1").start();

        new Thread(()->{
            synchronized (o) {
                for(char c : aC) {
                    System.out.print(c);
                    //latch.countDown()
                    t2Started = true;
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        }, "t2").start();
    }
}


