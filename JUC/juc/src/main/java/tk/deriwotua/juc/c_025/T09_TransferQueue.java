package tk.deriwotua.juc.c_025;

import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue �̼߳�����񴫵�
 * 	�����ݲ�����к��Զ�����ֱ����ȡ������
 * 	�����ڴ���ĳ���߼�����Ҫ�õ�һ���������ܼ���ִ�������߼�
 */
public class T09_TransferQueue {
	public static void main(String[] args) throws InterruptedException {
		LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();
		
		new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		/**
		 * ��put�������������������������ȴ���ȡ��
		 */
		strs.transfer("aaa");

		/**
		 * �����������
		 */
		//strs.put("aaa");

		/*new Thread(() -> {
			try {
				System.out.println(strs.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/


	}
}
