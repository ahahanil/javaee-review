package tk.deriwotua.thread.communication.num;

/**
 * 一个线程输出 10 次 1，一个线程输出 10 次 2，要求交替输出“1 2 1 2 1 2...”或“2 1 2 1 2 1...”
 */
public class TestThreadForNum {
    public static void main(String[] args) {
        new ThreadForNum1().start();
        new ThreadForNum2().start();
    }
}
