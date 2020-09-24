/**
 * 写时复制容器 copy on write
 * 多线程环境下，写时效率低，读时效率高
 * 适合写少读多的环境
 * 
 * 
 * 
 * @author 马士兵
 */
package tk.deriwotua.juc.c_025_Queue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList 写时复制容器
 * 	从容器中读数据时是不加锁的
 * 		不加锁原因在于新容器基于旧容器拷贝的数据一致的
 * 	往容器java.util.concurrent.CopyOnWriteArrayList#add(java.lang.Object)写数据时加锁
 * 		synchronized
 * 		写数据时先把容器拷贝(拷贝时容量加一)然后在尾部插入要添加的元素
 * 		然后再把指向旧容器指针指向复制的这份
 *
 */
public class T02_CopyOnWriteList {
	public static void main(String[] args) {
		List<String> lists = 
				//new ArrayList<>(); //这个会出并发问题！
				//new Vector();
				new CopyOnWriteArrayList<>();
		Random r = new Random();
		Thread[] ths = new Thread[100];
		/**
		 * 每个线程往lists里放1000个元素
		 */
		for(int i=0; i<ths.length; i++) {
			Runnable task = new Runnable() {
	
				@Override
				public void run() {
					for(int i=0; i<1000; i++) lists.add("a" + r.nextInt(10000));
				}
				
			};
			ths[i] = new Thread(task);
		}
		
		
		runAndComputeTime(ths);
		
		System.out.println(lists.size());
	}
	
	static void runAndComputeTime(Thread[] ths) {
		long s1 = System.currentTimeMillis();
		Arrays.asList(ths).forEach(t->t.start());
		Arrays.asList(ths).forEach(t->{
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		long s2 = System.currentTimeMillis();
		System.out.println(s2 - s1);
		
	}
}

