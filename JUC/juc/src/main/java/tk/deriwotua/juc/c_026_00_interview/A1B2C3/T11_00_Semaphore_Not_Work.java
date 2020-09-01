package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

import java.util.concurrent.Semaphore;

/**
 * Semaphore 用来限流(限制线程运行个数)
 *  两个线程交替打印通过 Semaphore 限制只能一个线程运行
 *      但是问题是没法做到限制哪个运行所以无法实现
 *   交替打印主要在于顺序执行问题
 *   而 Semaphore 用来处理并发问题进行限流
 *   两个是不同同类型
 */
public class T11_00_Semaphore_Not_Work {

    public static void main(String[] args) {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        /**
         * 限流 每次只允许1个线程运行
         */
        Semaphore semaphore = new Semaphore(1, true);

        /**
         * 只能保障每次只允许某个线程运行 但是没法保障交换执行所以不行
         */
        new Thread(()->{
            for(char c : aI) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(c);
                semaphore.release();
            }
        }, "t1").start();

        new Thread(()->{
            for(char c : aC) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(c);
                semaphore.release();
            }
        }, "t2").start();
    }
}
