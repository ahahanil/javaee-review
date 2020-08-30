package tk.deriwotua.juc.c_025;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue �̼߳�ͬ�����ݴ��ݶ���(��������)
 * 	����ĳ���̸߳�����һ���߳��´������
 * 	SynchronousQueue �Ƚ���������Ϊ0
 * 		��������add() ���쳣����
 * 	�÷� ��ͨ�� put()��������ǰ��Ҫ�������̵߳���take()ȡ
 * 		�̼߳��޴洢ʽ������Ͷ�ݸ�����һ���߳�(�̼߳�ֱ���´�����)
 * 		û���߳�take()ȡʱһֱ�ȴ�(put()���������������ʽ��)
 * 	    �÷��ϱ�ʹ��Exchange������Ķ�
 *
 * 	    �̳߳����ô����Ķ���
 * 	    	�̼߳����ʱʹ�õĶ��� SynchronousQueue
 */
public class T08_SynchronousQueue { //����Ϊ0
	public static void main(String[] args) throws InterruptedException {
		/**
		 * Ҳ����������
		 */
		BlockingQueue<String> strs = new SynchronousQueue<>();
		
		new Thread(()->{
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		/**
		 * �����ȴ�����������
		 */
		strs.put("aaa");
		//strs.put("bbb");
		//strs.add("aaa");
		System.out.println(strs.size());
	}
}
