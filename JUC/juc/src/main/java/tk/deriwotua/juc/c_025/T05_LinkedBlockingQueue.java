package tk.deriwotua.juc.c_025;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingQueue �޽���������
 * 	�� Queue ������ʵ��������ʽput()��take()����
 * 	����ʵ�ֵ���������
 * 	��������������������ȴ�ֱ���пռ����
 * 	���пպ���ȡ�������ȴ�ֱ����ȡ������
 * 	��� LinkedBlockingQueue ��Ȼ�Ѻ�������&������ģ��
 * 		�ײ���� LockSupport#park()������LockSupport#unpark()����
 */
public class T05_LinkedBlockingQueue {

	static BlockingQueue<String> strs = new LinkedBlockingQueue<>();

	static Random r = new Random();

	public static void main(String[] args) {
		/**
		 * ������
		 */
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				try {
					/**
					 * ���������������
					 * ������������������ȴ�ֱ���пռ����
					 */
					strs.put("a" + i);
					TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "p1").start();

		/**
		 * ������
		 */
		for (int i = 0; i < 5; i++) {
			new Thread(() -> {
				for (;;) {
					try {
						/**
						 * �Ӷ�����ȡ��ͷԪ��(ɾ������)
						 * ������п����������ȴ���ȡ������Ϊֹ
						 */
						System.out.println(Thread.currentThread().getName() + " take -" + strs.take()); //������ˣ��ͻ�ȴ�
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "c" + i).start();

		}
	}
}
