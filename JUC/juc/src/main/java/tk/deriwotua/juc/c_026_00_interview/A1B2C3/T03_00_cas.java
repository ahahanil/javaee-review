package tk.deriwotua.juc.c_026_00_interview.A1B2C3;

/**
 * CAS 方式实现两个线程交替打印
 *  先声明线程共享的变量 r 标记当前执行的线程
 *  然后启动线程两个线程 线程中死循环判断总有一个线程的值与 r 相等
 *      相等的线程先 打印 然后变更 r 值为另一个线程
 *  开始与 r 不相等的线程死循环阻塞直到 另一个线程变更 r 值使得 死循环条件不满足跳出循环执行
 * 由此也能看出 CAS 是占用CPU资源的
 */
public class T03_00_cas {

    /**
     * 声明两个线程
     */
    enum ReadyToRun {T1, T2}

    /**
     * 思考为什么必须volatile
     *  线程间可见性
     */
    static volatile ReadyToRun r = ReadyToRun.T1;

    public static void main(String[] args) {

        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        new Thread(() -> {

            for (char c : aI) {
                /**
                 * 如果不是 t1 阻塞等待
                 */
                while (r != ReadyToRun.T1) {}
                System.out.print(c);
                r = ReadyToRun.T2;
            }

        }, "t1").start();

        new Thread(() -> {
            for (char c : aC) {
                /**
                 * 如果不是 t2 阻塞等待
                 */
                while (r != ReadyToRun.T2) {}
                System.out.print(c);
                r = ReadyToRun.T1;
            }
        }, "t2").start();
    }
}


