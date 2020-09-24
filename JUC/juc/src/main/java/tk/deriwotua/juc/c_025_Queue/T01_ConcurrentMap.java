package tk.deriwotua.juc.c_025_Queue;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * ConcurrentHashMap ����hash��߲�������
 * 	����CAS���� Ҳ�����������API�в�û��ConcurrentTreeMap(������CAS�����ǳ�����)
 * 	�����Ǿ���Ҫ����ĸ߲���Map��ô������ ConcurrentSkipListMap ����ͳ�����
 * ConcurrentSkipListMap ����߲�����������
 * 	�ײ�����(����ʱ�临�Ӷ�O(n)����ʱ�临�Ӷ�O(lgN)) ������ô�������ʱ�临�Ӷȸ�����
 * 	���� �ײ���������������ȡ���ֽڵ�Ԫ�طֲ�ֲ�����ݻ��Ƚ϶�����ȡ�ֲ�
 * 		1------------->56------------->97
 * 		1----->35----->56----->79----->97
 * 		1->23->35->42->56->67->79->80->97
 *	�������67Ԫ�����ȴӶ�����ҷ�����[56,97]����
 *	����ڶ���ֻ�ò���[56,97]���䷢����[56,79]����
 *  ������������[56,79]������ҵ�67
 *  �ڵ������ʵ��������Ч�ʱȵ�������߶��һ���CAS��������ʵ��Ҫ�򵥵Ķ�
 * TreeMap ����(�����ƽ��������Ч�ʸ�)
 */
public class T01_ConcurrentMap {
	public static void main(String[] args) {
		Map<String, String> map = new ConcurrentHashMap<>();
//		Map<String, String> map = new ConcurrentSkipListMap<>(); //�߲�����������
		
//		Map<String, String> map = new Hashtable<>();
//		Map<String, String> map = new HashMap<>(); //Collections.synchronizedXXX
		//TreeMap
		Random r = new Random();
		Thread[] ths = new Thread[100];
		CountDownLatch latch = new CountDownLatch(ths.length);
		long start = System.currentTimeMillis();
		for(int i=0; i<ths.length; i++) {
			ths[i] = new Thread(()->{
				for(int j=0; j<10000; j++) map.put("a" + r.nextInt(100000), "a" + r.nextInt(100000));
				latch.countDown();
			});
		}
		
		Arrays.asList(ths).forEach(t->t.start());
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(map.size());

	}
}
