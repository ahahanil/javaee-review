package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic 原子操作实现线程间交替打印
 *  与 CAS 方式实现类似
 */
public class T05_00_AtomicInteger {

    static AtomicInteger threadNo = new AtomicInteger(1);

    public static void main(String[] args) {

        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(() -> {
            for (char c : aI) {
                while (threadNo.get() != 1) {}
                System.out.print(c);
                threadNo.set(2);
            }
        }, "t1").start();

        new Thread(() -> {
            for (char c : aC) {
                while (threadNo.get() != 2) {}
                System.out.print(c);
                threadNo.set(1);
            }
        }, "t2").start();
    }
}


