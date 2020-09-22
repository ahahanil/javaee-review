package tk.deriwotua.juc.c_022_RefTypeAndThreadLocal;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal
 * 	public T get() { } 获取ThreadLocal在当前线程中保存的变量副本
 *  public void set(T value) { } 设置当前线程中变量的副本
 *  public void remove() { } 移除当前线程中变量的副本
 *  protected T initialValue() { } 延迟加载方法
 * ![弱引用之ThreadLocal](../assets/弱引用之ThreadLocal.png)
 *
 * 有两个变量a和b，存放在map里面，a是key（但是a被弱引用包装了一下），b是value。如果map一直存在，a和b因为被map关联，则b就一直不能被回收，但是a可以被回收。一旦a回收了，那么无法通过a找到b了，这就是b出现内存泄漏。
 */
public class _02ThreadLocal {

	//volatile static Person p = new Person();

	static ThreadLocal<Person> tl = new ThreadLocal<>();
	
	public static void main(String[] args) {
				
		new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/**
			 * 首先 Thread类中存在 ThreadLocal.ThreadLocalMap threadLocals变量
			 * 	ThreadLocalMap类存在继承自指定了ThreadLocal<?>泛型的WeakReference<ThreadLocal<?>>弱引用类的Entry
			 * 		Entry<T, Object> 单对
			 * 在ThreadLocal#get()时先获取当前线程ThreadLocal.ThreadLocalMap类
			 * 	当 ThreadLocal.ThreadLocalMap 不为null时 取出弱引用 Entry<T, Object> 的 Object类型的值(创建ThreadLocal指定的泛型)
			 * 	为 null 时 java.lang.ThreadLocal#setInitialValue()方法初始化值
			 * 		内部会调用java.lang.ThreadLocal#initialValue()获取value初始值可重写
			 * 		通过java.lang.ThreadLocal#createMap()创建指定大小的 Entry[16] 数组
			 * 		然后获取ThreadLocal实例的 hash值通过和数组指定大小与运算取得一个数组下标 i
			 * 			把当前 ThreadLocal实例作为key 存入 Entry<threadLocal, value>
			 * 		记录Entry[]数组的存放元素个数
			 */
			System.out.println(tl.get());
		}).start();
		
		new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/**
			 * java.lang.ThreadLocal#set() 取出当前线程的 ThreadLocalMap
			 * 通过 hash & 数组长度 计算出存放下标位置
			 * 	该位置不存在元素时直接赋值存入
			 * 	存在则比对key相同更新value
			 * 	当key 为 null (由于采用弱引用很容易会被回收)
			 * 		通过 java.lang.ThreadLocal.ThreadLocalMap#replaceStaleEntry() 查找相邻的下标且相同key值然后更新value
			 * 		都找不到直接创建Entry<key, value> 插入某个下标位置（大意）
			 * 	内部有着 HashMap扩容一样逻辑
			 */
			tl.set(new Person());
		}).start();

		/**
		 * ThreadLocal 内存泄漏的可能
		 * 	t1 强引用被释放后由于 ThreadLocalMap#Entry的key弱引用指向 ThreadLocal 所以在t1释放后GC时会回收key弱引用指向空间
		 * 	但是key 被回收后 就无法通过key直到对应的value值存在内存泄漏
		 * 	所以使用ThreadLocal最后需要手动remove()
		 */
		tl.remove();
	}
	
	static class Person {
		String name = "zhangsan";
	}
}


