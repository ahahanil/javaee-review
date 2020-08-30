package tk.deriwotua.juc.c_023_02_FromHashtableToCHM;

import java.util.Hashtable;
import java.util.UUID;

/**
 * 线程安全Map
 *  Hashtable 都是 synchronized 方法 自带锁
 */
public class T01_TestHashtable {

    static Hashtable<UUID, UUID> m = new Hashtable<>();

    static int count = Constants.COUNT;
    /**
     * 一百万个键值对
     */
    static UUID[] keys = new UUID[count];
    static UUID[] values = new UUID[count];
    /**
     * 100个线程
     */
    static final int THREAD_COUNT = Constants.THREAD_COUNT;

    static {
        for (int i = 0; i < count; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    /**
     * 每个线程往Hashtable里放1万个键值对
     */
    static class MyThread extends Thread {
        int start;
        /**
         * 每个线程负责1万个键值对
         */
        int gap = count/THREAD_COUNT;

        public MyThread(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for(int i=start; i<start+gap; i++) {
                m.put(keys[i], values[i]);
            }
        }
    }

    public static void main(String[] args) {
        /**
         * 模拟一百万个键值对插入到Hashtable里
         */
        long start = System.currentTimeMillis();

        /**
         * 100个线程
         */
        Thread[] threads = new Thread[THREAD_COUNT];

        for(int i=0; i<threads.length; i++) {
            // 每个线程起始值
            threads[i] = new MyThread(i * (count/THREAD_COUNT));
        }

        for(Thread t : threads) {
            t.start();
        }

        /**
         * 等待每个线程结束
         */
        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));

        System.out.println("容器内键值对个数：" + m.size());

        //-----------------------------------
        /**
         * 模拟读取Hashtable一千万个键值对
         *  差不多要30s+
         */
        start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 10000000; j++) {
                    m.get(keys[10]);
                }
            });
        }

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
