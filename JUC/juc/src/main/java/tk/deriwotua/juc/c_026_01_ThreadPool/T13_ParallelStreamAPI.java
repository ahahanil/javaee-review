package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ParallelStreamAPI ������ʽAPI
 * 	�ײ� ForkJoinPoolʵ�ֵ�
 * 	�ڲ���������зֲ�������ִ��
 * 	������ʱ�̼߳䲻��Ҫͬ��ʹ�ò�����ʽ���������Ч��(���lambda���ʽ���������ı�Ч��)
 */
public class T13_ParallelStreamAPI {
	public static void main(String[] args) {
		/**
		 * ���������һ��������
		 */
		List<Integer> nums = new ArrayList<>();
		Random r = new Random();
		for(int i=0; i<10000; i++) nums.add(1000000 + r.nextInt(1000000));
		
		//System.out.println(nums);
		
		long start = System.currentTimeMillis();
		/**
		 * lambda ��ʽ�����Ƿ�������
		 */
		nums.forEach(v->isPrime(v));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		start = System.currentTimeMillis();
		/**
		 * ʹ��parallel stream api
		 * ��������ʽ�����Ƿ�������
		 * 	���������ڲ���������зֲ�������ִ��
		 */
		nums.parallelStream().forEach(T13_ParallelStreamAPI::isPrime);
		end = System.currentTimeMillis();
		
		System.out.println(end - start);
		/**
		 * ��������Ľ���ᷢ��
		 * 	parallelStream Ч���� lambda��ʽ�����ı�
		 */
	}

	/**
	 * �Ƿ�������
	 * @param num
	 * @return
	 */
	static boolean isPrime(int num) {
		for(int i=2; i<=num/2; i++) {
			if(num % i == 0) return false;
		}
		return true;
	}
}
