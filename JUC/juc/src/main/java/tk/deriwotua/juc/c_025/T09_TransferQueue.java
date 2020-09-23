package tk.deriwotua.juc.c_025;

import java.util.concurrent.LinkedTransferQueue;

/**
 * LinkedTransferQueue �̼߳�����񴫵�
 * 	�����߻�һֱ����ֱ������ӵ����е�Ԫ�ر�ĳһ��������������
 * 	�������Ƿ�����Ԫ�ش�һ���߳� transfer ����һ���̵߳Ĺ����У�����Ч��ʵ����Ԫ�����߳�֮��Ĵ���
 * 	LinkedTransferQueue ʵ������ ConcurrentLinkedQueue��SynchronousQueue����ƽģʽ���� LinkedBlockingQueue �ĳ���
 *
 *
 *  transfer(E e)������ǰ����һ�����ڵȴ���ȡ���������̣߳��������ƽ�֮��
 *  	���򣬻���뵱ǰԪ��e������β�������ҵȴ���������״̬�������������߳�ȡ�߸�Ԫ�ء�
 *  tryTransfer(E e)������ǰ����һ�����ڵȴ���ȡ���������̣߳�ʹ��take()����poll()��������ʹ�ø÷����ἴ��ת��/�������Ԫ��e���������ڣ��򷵻�false�����Ҳ�������С�����һ���������Ĳ�����
 *  tryTransfer(E e, long timeout, TimeUnit unit)������ǰ����һ�����ڵȴ���ȡ���������̣߳��������������;
 * 		���򽫲���Ԫ��e������β�������ҵȴ����������̻߳�ȡ���ѵ�������ָ����ʱ����Ԫ��e�޷����������̻߳�ȡ���򷵻�false��ͬʱ��Ԫ�ر��Ƴ���
 *  hasWaitingConsumer()���ж��Ƿ�����������̡߳�
 *  getWaitingConsumerCount()����ȡ���еȴ���ȡԪ�ص������߳�������
 *
 * 	�ײ�ʹ��LockSupport#park() LockSupport#unpark()
 *
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
		 * ����ʽ�Ľ����ݴ�һ���̴߳��ݵ���һ���߳�
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
