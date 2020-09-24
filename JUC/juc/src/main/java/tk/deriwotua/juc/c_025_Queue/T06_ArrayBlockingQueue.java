package tk.deriwotua.juc.c_025_Queue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ArrayBlockingQueue �н���������
 * 	��������put()������������ȴ�ֱ���пռ����
 *         offer()����ӷ���false ���ʧ��
 *  ���пպ�take()��ȡ�������ȴ�ֱ����ȡ������
 *         poll()����null
 */
public class T06_ArrayBlockingQueue {

	static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);

	static Random r = new Random();

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			strs.put("a" + i);
		}

		/**
		 * ���в�������ʱ
		 * ����������˾ͻ������ȴ�
		 */
		//strs.put("aaa");
		/**
		 * ���в�������ʱ
		 * ����������˾ͻ����쳣
		 */
		//strs.add("aaa");
		/**
		 * ���в�������ʱ
		 * ����������˾������᷵��false
		 */
		//strs.offer("aaa");
		/**
		 * ���в�������ʱ
		 * ����������˾͵�1s�ٳ��Բ��뻹���ɹ��᷵��false
		 */
		strs.offer("aaa", 1, TimeUnit.SECONDS);
		
		System.out.println(strs);
	}
}
