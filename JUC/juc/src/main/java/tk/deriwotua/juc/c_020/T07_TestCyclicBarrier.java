package tk.deriwotua.juc.c_020;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier ͬ������
 * ������һ���̴߳ﵽһ������ʱ��������ֱ�����һ���̴߳ﵽ����(���������̶߳���λ)ʱ�����б��������̲߳��ܼ���ִ��
 * ����դ�����������Ա��´�ʹ��
 * ���� ReentrantLock ʵ��
 * ��Ҳά���� count ������ÿ�ε���java.util.concurrent.CyclicBarrier#await()���Լ�ֱ��0
 */
public class T07_TestCyclicBarrier {
    public static void main(String[] args) {

        //CyclicBarrier barrier = new CyclicBarrier(20);
        /**
         * �����������ص��߳�����
         * ���������Ϻ�ִ��Runnable����
         */
        CyclicBarrier barrier = new CyclicBarrier(20, () -> System.out.println("����"));

        /*CyclicBarrier barrier = new CyclicBarrier(20, new Runnable() {
            @Override
            public void run() {
                System.out.println("���ˣ�����");
            }
        });*/

        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                try {
                    /**
                     * ��ǰ�߳��Ѿ���������λ��(��λ)���̱߳�������ֱ�������̵߳������
                     * �ڲ����� java.util.concurrent.CyclicBarrier#dowait()
                     *  ִ��ʱ��ʹ��ReentrantLock#lock()����
                     *  ���浱ǰ Generation Ϊ g
                     *  Ȼ��java.util.concurrent.CyclicBarrier.Generation#broken�ж�դ���Ƿ���ֱ���׳��쳣
                     *      ��һ���̴߳��ڵȴ�״̬��һ���̵߳�����������������Ϊ��ʼ״̬ʱդ���𻵻��߳����쳣��ȴ�ʱ���ж���
                     *  Ȼ��������Լ��ڻ�ȡ������ֵ Ϊ0 �����һ���߳̾�λ
                     *      ִ��դ������
                     *      ����֮ǰ�ȴ����߳�
                     *      count���� generation����
                     *      �ͷ���
                     *  �������û��ʱ�����ƣ���ֱ�ӵȴ���ֱ��������
                     *  ���Ѻ����ִ��
                     *      g != generation ��ʾ����������(generation������)�����ص�ǰ�߳�����դ�����±�
                     *     ��� g == generation��˵����û�л�������Ϊʲô�����ˣ�
                     *     ��Ϊһ���߳̿���ʹ�ö��դ���������դ������������̣߳��ͻ��ߵ����������Ҫ�ж��Ƿ��ǵ�ǰ����
                     *     ������Ϊ���ԭ�򣬲���Ҫgeneration����֤��ȷ��
                     */
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
/**
 * CountDownLatch��CyclicBarrier����ͬ
 *   ���ص㲻ͬ��CountDownLatchһ������һ���̵߳ȴ�һ�������̣߳���CyclicBarrierһ����һ���̼߳���໥�ȴ���ĳͬ���㣻
 *   CyclicBarrier�ļ������ǿ������ã���CountDownLatch�����ԡ�
 *   CyclicBarrier�ܹ���ø����߳�״̬��Ϣ
 */
