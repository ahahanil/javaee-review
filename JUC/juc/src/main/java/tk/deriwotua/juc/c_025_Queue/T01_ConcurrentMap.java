package tk.deriwotua.juc.c_025_Queue;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * ConcurrentHashMap 基于hash表高并发容器
 * 	基于CAS操作 也正是由于这个API中并没有ConcurrentTreeMap(在树中CAS操作非常复杂)
 * 	问题是就需要有序的高并发Map怎么办所以 ConcurrentSkipListMap 条表就出现了
 * ConcurrentSkipListMap 跳表高并发容器有序
 * 	底层链表(链表时间复杂度O(n)而树时间复杂度O(lgN)) 所以怎么解决链表时间复杂度高问题
 * 	做法 底层链表排序后从中提取部分节点元素分层分层后数据还比较多再提取分层
 * 		1------------->56------------->97
 * 		1----->35----->56----->79----->97
 * 		1->23->35->42->56->67->79->80->97
 *	假如查找67元素首先从顶层查找发现在[56,97]区间
 *	而后第二层只用查找[56,97]区间发现在[56,79]区间
 *  而后第三层就在[56,79]区间可找到67
 *  在调表这个实现上首先效率比单纯链表高而且基于CAS操作比树实现要简单的多
 * TreeMap 有序(红黑树平衡树查找效率高)
 */
public class T01_ConcurrentMap {
	public static void main(String[] args) {
		Map<String, String> map = new ConcurrentHashMap<>();
//		Map<String, String> map = new ConcurrentSkipListMap<>(); //高并发并且排序
		
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
