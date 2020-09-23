/**
 * ��N�Ż�Ʊ��ÿ��Ʊ����һ�����
 * ͬʱ��10�����ڶ�����Ʊ
 * ��дһ��ģ�����
 * 
 * ��������ĳ�����ܻ������Щ���⣿
 * �ظ����ۣ��������ۣ�
 * 
 * ʹ��Vector����Collections.synchronizedXXX
 * ����һ�£������ܽ��������
 * 
 * �������A��B����ͬ���ģ���A��B��ɵĸ��ϲ���Ҳδ����ͬ���ģ���Ȼ��Ҫ�Լ�����ͬ��
 * ������������ж�size�ͽ���remove������һ������ԭ�Ӳ���
 * 
 * ʹ��ConcurrentQueue��߲�����
 * 
 * @author ��ʿ��
 */
package tk.deriwotua.juc.c_024_FromVectorToQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue �̰߳�ȫ����ģ����Ʊ
 * 	����CAS�����̰߳�ȫ �� ������ �� �޽� ����
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
public class TicketSeller4 {
	static Queue<String> tickets = new ConcurrentLinkedQueue<>();

	static {
		for(int i=0; i<1000; i++) tickets.add("Ʊ ��ţ�" + i);
	}
	
	public static void main(String[] args) {
		
		for(int i=0; i<10; i++) {
			new Thread(()->{
				while(true) {
					/**
					 * �Ӷ�����ȡ��ͷֵ(������)
					 */
					String s = tickets.poll();
					// �����Ϊnull
					if(s == null) break;
					else System.out.println("������--" + s);
				}
			}).start();
		}
	}
}
