package tk.deriwotua.juc.c_018_00_AtomicXXX;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder: 50000000 time 368
 * Sync: 50000000 time 134
 *
 * Process finished with exit code 0
 *
 * LongAdder: 500000000 time 4349
 * Sync: 500000000 time 1764
 *
 * Process finished with exit code 0
 *
 * synchronized 偏向锁可以避免过多CAS判断操作
 */
public class T04_____SyncVsLongAdder {
    static long count2 = 0L;
    static LongAdder count = new LongAdder();

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[5000];

        for(int i=0; i<threads.length; i++) {
            threads[i] =
                    new Thread(()-> {
                        for(int k=0; k<100000; k++) count.increment();
                    });
        }

        long start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        long end = System.currentTimeMillis();

        //TimeUnit.SECONDS.sleep(10);

        System.out.println("LongAdder: " + count.longValue() + " time " + (end-start));
        //-----------------------------------------------------------
        Object lock = new Object();

        for(int i=0; i<threads.length; i++) {
            threads[i] =
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (lock) {
                            for(int k=0; k<100000; k++) {

                                count2++;
                            }
                        }
                    }
                });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads ) t.start();

        for (Thread t : threads) t.join();

        end = System.currentTimeMillis();


        System.out.println("Sync: " + count2 + " time " + (end-start));
    }

    static void microSleep(int m) {
        try {
            TimeUnit.MICROSECONDS.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
