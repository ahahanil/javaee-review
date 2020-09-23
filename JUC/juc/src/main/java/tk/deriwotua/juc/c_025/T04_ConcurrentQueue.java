package tk.deriwotua.juc.c_025;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue �൱�� BlockingQueue���̰߳�ȫ�Ĳ���
 *
 * ConcurrentLinkedQueue �̰߳�ȫ����ģ����Ʊ
 * 	����CAS�����̰߳�ȫ �� ������ �� �޽� ����
 * 		���������nullԪ��
 * ���̻߳����¶࿼�� Queue��Set
 *
 * ConcurrentLinkedQueue
 * 	  static final class Node<E> {
 *           volatile E item; // ������
 *           volatile Node<E> next; // next ָ��
 *    }
 *    ��Ҫ�ֶ�
 *    	private transient volatile Node<E> head; ͷ�ڵ�ָ��
 * 		private transient volatile Node<E> tail; β�ڵ�ָ��
 *
 * 		JDK9 ����java.lang.invoke.VarHandle ��ͨ��ָ��ֱ�Ӳ���ָ��ָ����ڴ�ռ������ԭ���Բ���ֱ�Ӳ�����������
 * 			�ֶζ���ʹ�� VarHandle ����
 * 				private static final VarHandle HEAD; ͷ�ڵ�ָ��
 *     			private static final VarHandle TAIL; β�ڵ�ָ��
 *     			static final VarHandle ITEM;
 *     			static final VarHandle NEXT;
 *    ���� VarHandle ָ���Ͳ���ʹ�� Unsafe CAS��ط�������ֱ��ʹ�� java.lang.invoke.VarHandle#compareAndSet()���CAS����
 * ConcurrentLinkedQueue���д���ͷβָ������һ��
 * 		offer �̺߳� poll �߳����߲��ޡ�������
 * 			poll ������ɾ��ͷ�ڵ�ͨ����Ҫ���� offer ����ж�β��ӣ���ͷɾ���ٶ�Ҫ���ڶ�β��ӽڵ���ٶȣ�������Ƕ��г��Ȼ�Խ��Խ��
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
