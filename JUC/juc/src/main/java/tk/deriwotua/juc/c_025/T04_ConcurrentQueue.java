package tk.deriwotua.juc.c_025;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue �൱�� BlockingQueue���̰߳�ȫ�Ĳ���
 */
public class T04_ConcurrentQueue {
	public static void main(String[] args) {
		Queue<String> strs = new ConcurrentLinkedQueue<>();
		
		for(int i=0; i<10; i++) {
			/**
			 * ��������Χ�ೢ�Բ���ɹ�����true(���add������ʱ�������쳣)
			 */
			strs.offer("a" + i);  //add
		}
		
		System.out.println(strs);
		
		System.out.println(strs.size());
		/**
		 * ȡ��ͷԪ��(ɾ������)
		 */
		System.out.println(strs.poll());
		System.out.println(strs.size());
		/**
		 * ȡͷԪ��(��ɾ��)
		 */
		System.out.println(strs.peek());
		System.out.println(strs.size());
		
		//˫�˶���Deque
	}
}
