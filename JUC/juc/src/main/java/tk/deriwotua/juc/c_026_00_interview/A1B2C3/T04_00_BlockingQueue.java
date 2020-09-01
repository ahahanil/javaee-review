package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列实现两个线程间交替打印
 *  put() 插入队列满了阻塞等待直到有空间可插入
 *  take() 取出头元素空队列时阻塞等待直到能取出
 *
 *  启动两个线程 t1、t2
 *      t2 线程启动先从 q1 阻塞队列里 take()
 *          由于q1没有数据 所以 t2 等待
 *      t1 线程启动后先打印一个字符然后往 q1 阻塞队列里 put() 插入元素
 *          插入后 从 q2 阻塞队列里 take() 由于q2没有数据 所以 t1 等待
 *          然而q1插入数据后使得 t2 线程可用从 q1 中 take() 出数据因此 t2 继续向下执行
 *      t2 继续执行打印字符然后往 q2 中 put() 插入元素 换得 t1 线程执行
 *      如此反复
 */
public class T04_00_BlockingQueue {

    /**
     * 数组实现阻塞队列 容量为1
     */
    static BlockingQueue<String> q1 = new ArrayBlockingQueue(1);
    static BlockingQueue<String> q2 = new ArrayBlockingQueue(1);

    public static void main(String[] args) throws Exception {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(() -> {
            for(char c : aI) {
                System.out.print(c);
                try {
                    q1.put("ok");
                    q2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            for(char c : aC) {
                try {
                    q1.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(c);
                try {
                    q2.put("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();
    }
}