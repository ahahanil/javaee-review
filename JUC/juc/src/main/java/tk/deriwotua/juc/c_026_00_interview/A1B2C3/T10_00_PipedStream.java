package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 线程间管道通信
 *  一个线程先从通道里阻塞式读然后再写
 *  另一个线程先往通道里写然后再读
 *  生产者消费者模型
 */
public class T10_00_PipedStream {

    public static void main(String[] args) throws Exception {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        PipedInputStream input1 = new PipedInputStream();
        PipedInputStream input2 = new PipedInputStream();
        PipedOutputStream output1 = new PipedOutputStream();
        PipedOutputStream output2 = new PipedOutputStream();

        /**
         * 线程的输入连接另一个线程输出
         */
        input1.connect(output2);
        input2.connect(output1);

        /**
         * 消息
         */
        String msg = "Your Turn";

        new Thread(() -> {
            byte[] buffer = new byte[9];
            try {
                for(char c : aI) {
                    /**
                     * 阻塞式从另一个线程读取数据
                     */
                    input1.read(buffer);
                    if(new String(buffer).equals(msg)) {
                        System.out.print(c);
                    }
                    // 然后阻塞式写
                    output1.write(msg.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t1").start();

        new Thread(() -> {
            byte[] buffer = new byte[9];
            try {
                for(char c : aC) {
                    System.out.print(c);
                    /**
                     * 阻塞式往另一个线程写数据
                     */
                    output2.write(msg.getBytes());
                    /**
                     * 阻塞式从另一个线程读取数据
                     */
                    input2.read(buffer);
                    if(new String(buffer).equals(msg)) {
                        continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
}