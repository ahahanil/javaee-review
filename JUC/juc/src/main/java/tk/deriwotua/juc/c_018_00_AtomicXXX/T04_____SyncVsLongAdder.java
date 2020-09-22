package tk.deriwotua.juc.c_018_00_AtomicXXX;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

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
                         *
                         */
                        /*synchronized (lock) {
                            for(int k=0; k<100000; k++) {
                                count2++;
                            }
                        }*/
                        /**
                         * 这两种写法意义是不一样的
                         *  上面是自增100000为一个锁操作(类似一个原子操作)
                         *  下面是每次自增为一个锁操作(类似一个原子操作)
                         *  LongAdder#increment()相当于是每次自增看做一个原子操作
                         *  这里比较的是每次原子自增操作基础上自增100000次所有上面写法不符合题意
                         *  比较是没有意义的
                         */
                        for(int k=0; k<100000; k++) {
                            synchronized (lock) {
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
