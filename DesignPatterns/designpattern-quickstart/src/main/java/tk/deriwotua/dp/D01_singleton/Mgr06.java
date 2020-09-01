package tk.deriwotua.dp.D01_singleton;

/**
 * lazy loading
 * 也称懒汉式
 * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * 可以通过synchronized解决，但也带来效率下降
 * 而且每次都要判断。那么有没有更好的方式实现呢？
 *  双重检查加锁
 * 双重检查加锁并不是每次进入getInstance方法都需要同步，而是先不同步，进入方法后，先检查实例是否存在，
 * 如果不存在才进行下面的同步块，这是第一重检查，进入同步块过后，
 * 再次检查实例是否存在，如果不存在，就在同步的情况下创建一个实例，这是第二重检查。
 * 这样一来，就只需要同步一次了，从而减少了多次在同步情况下进行判断所浪费的时间。
 */
public class Mgr06 {
    /**
     * volatile 保证禁止指令重排、线程可见性
     *  运行时所需的数据从主存复制一份到CPU的高速缓存当中比如这里的INSTANCE变量
     *  而当某个线程更新了此变量(主存也更新了)但其它线程并不会立即从主存取而还是取得高速缓存中值
     *  所以需要缓存一致性协议
     *      当CPU写数据时，如果发现操作的变量是共享变量，即在其他CPU中也存在该变量的副本，会发出信号通知其他CPU将该变量的缓存行置为无效状态，
     *      因此当其他CPU需要读取这个变量时，发现自己缓存中缓存该变量的缓存行是无效的，那么它就会从内存重新读取。
     *  这也就是可见性当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值。
     *
     *  volatile关键字可能会屏蔽掉虚拟机中一些必要的代码优化，所以运行效率并不是很高。因此一般建议，没有特别的需要，不要使用。
     */
    private static volatile Mgr06 INSTANCE; //JIT

    private Mgr06() {
    }

    /**
     * 双重检查
     *  实现方式既可以实现线程安全地创建实例，而又不会对性能造成太大的影响。
     *  它只是第一次创建实例的时候同步，以后就不需要同步了，从而加快了运行速度。
     * @return
     */
    public static Mgr06 getInstance() {
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if (INSTANCE == null) {
            //同步块，线程安全的创建实例
            synchronized (Mgr06.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if(INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 创建对象时内部会生成四条指令 首先申请空间 然后构造方法初始化 然后赋值 最后INSTANCE指向此空间
                     * 由于存在多步指令 就存在JVM优化时进行指令重排 可能会导致首先申请空间后即INSTANCE指向此空间还未初始化赋值时 其他线程就取值情况
                     * 所以 INSTANCE 变量需要加上 volatile 禁止指令重排
                     */
                    INSTANCE = new Mgr06();
                }
            }
        }
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr06.getInstance().hashCode());
            }).start();
        }
    }
}
