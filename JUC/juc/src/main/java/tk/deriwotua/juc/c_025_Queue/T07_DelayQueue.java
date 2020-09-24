package tk.deriwotua.juc.c_025_Queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue ʱ�����ӳ��������(�޽���ʱ��������)
 * 	���� PriorityQueue ʵ��ʱ���������ڰ�ʱ���������
 * 	�����д�ŵ�Ԫ�ر�����ʵ�� java.util.concurrent.Delayed �ӿ�
 * 		�ұ�����һ�����������Ƴٶ೤ʱ��
 *  �ӳٽ�� PriorityQueue ���ȶ���
 *  	�ӳ�ʱ����ΪȨ��
 *  	���ӳ�С���ȳ�����
 *  take() ʱ��ȡ������Ԫ��Ϊ��,��������
 *  	��Ϊ��,������Ԫ�ص�delayʱ��ֵ
 *  	Ϊ0�Ļ�,˵����Ԫ���Ѿ����˿���ʹ�õ�ʱ��,����poll����������Ԫ��,��������
 *  	��
 */
public class T07_DelayQueue {
	/**
	 * DelayQueue ������������
	 */
	static BlockingQueue<MyTask> tasks = new DelayQueue<>();

	static Random r = new Random();

	/**
	 * DelayQueue ��ŵ��������ʵ��Delayed�ӿ�
	 * 	�ұ�����һ�����������Ƴٶ೤ʱ��
	 */
	static class MyTask implements Delayed {
		String name;
		/**
		 * �Ƴٶ೤ʱ��
		 */
		long runningTime;
		
		MyTask(String name, long rt) {
			this.name = name;
			this.runningTime = rt;
		}

		/**
		 * ʱ���������Դ��ڱȽ��Ǹ�����ʱ����̼���ʱ�䳤������
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Delayed o) {
			if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS))
				return -1;
			else if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) 
				return 1;
			else 
				return 0;
		}

		/**
		 * �Ը�����ʱ�䵥λ������˶��������ʣ���ӳ�
		 * @param unit
		 * @return
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
		
		@Override
		public String toString() {
			return name + " " + runningTime;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		long now = System.currentTimeMillis();
		// ��ǰʱ��֮��1s����
		MyTask t1 = new MyTask("t1", now + 1000);
		// ��ǰʱ��֮��2s����
		MyTask t2 = new MyTask("t2", now + 2000);
		MyTask t3 = new MyTask("t3", now + 1500);
		MyTask t4 = new MyTask("t4", now + 2500);
		MyTask t5 = new MyTask("t5", now + 500);

		/**
		 * �������
		 */
		tasks.put(t1);
		tasks.put(t2);
		tasks.put(t3);
		tasks.put(t4);
		tasks.put(t5);
		
		System.out.println(tasks);

		/**
		 * һ������Ƚ��ȳ���������ʹ�õ��ǻ����ӳ�����Ķ���
		 * �����ӳ�С���ȳ����� ���γ�����
		 * 	t5 t1 t3 t2 t4
		 */
		for(int i=0; i<5; i++) {
			System.out.println(tasks.take());
		}
	}
}
