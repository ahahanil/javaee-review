/**
 * ThreadLocal�ֲ߳̾�����
 *
 * @author ��ʿ��
 */
package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.util.concurrent.TimeUnit;

public class _01ThreadLocal {
    /**
     * volatile 仅仅是 p 线程可见
     *  p.name 还是线程不可见的
     *  即线程存在私有空间
     */
    volatile static Person p = new Person();

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                /**
                 * 注意休眠时间差上会导致不同结果
                 */
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(p.name);
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.name = "lisi";
        }).start();
    }
}

class Person {
    String name = "zhangsan";
}
