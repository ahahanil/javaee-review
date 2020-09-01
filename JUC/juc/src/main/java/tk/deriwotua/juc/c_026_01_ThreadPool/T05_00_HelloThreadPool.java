package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor 继承于 AbstractExecutorService
 *  线程池主要维护两个集合
 *      线程集合
 *      任务集合
 */
public class T05_00_HelloThreadPool {

    /**
     * 任务类
     */
    static class Task implements Runnable {
        /**
         * 任务编号
         */
        private int i;

        public Task(int i) {
            this.i = i;
        }

        /**
         * 每个任务任务是打印编号 打印后阻塞
         */
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Task " + i);
            try {
                // 阻塞等待
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Task{" +
                    "i=" + i +
                    '}';
        }
    }

    public static void main(String[] args) {
        /**
         * 自定义线程池(7个参数)
         *  最大4个线程 + 等待队列可容4个任务 tpe线程池最大只能处理8个任务
         */
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                // 核心线程数(初始线程数) 一般参与空闲也不归还操作系统可配置参与归还逻辑
                2,
                // 最大线程数
                4,
                // 保持活跃 单位秒(当部分线程一段时间后还空闲归还操作系统核心线程除外)
                60, TimeUnit.SECONDS,
                /**
                 * 任务队列(可指定队列容量 这里为4)
                 * 使用不同类型的任务队列可以产生不同类型的线程池
                 */
                new ArrayBlockingQueue<Runnable>(4),
                /**
                 * 线程工厂
                 *  可自定义特定线程工厂
                 *  默认线程工厂创建线程会指定线程名称设置为非守护线程及线程优先级
                 *  指定线程名由于便于回溯
                 */
                Executors.defaultThreadFactory(),
                /**
                 * 拒绝策略
                 *  当线程池忙且等待队列也满了 此后任务执行拒绝策略
                 *  拒绝策略默认四种也可自定义
                 *      Abort       抛异常
                 *      Discard     扔掉不抛异常
                 *      DiscardOldest 扔掉排队时最久的任务
                 *      CallerRuns  调用者线程处理任务(这里即主线程去执行该任务)
                 *  应用上都是用自定义策略
                 */
                new ThreadPoolExecutor.CallerRunsPolicy());

        /**
         * 上来处理8个任务
         * 由于上面定义的Task任务打印任务号后都阻塞不释放
         * 所以执行后线程池已满负荷
         */
        for (int i = 0; i < 8; i++) {
            tpe.execute(new Task(i));
        }

        System.out.println(tpe.getQueue());
        /**
         * 线程池已满负荷再添加任务会触发拒绝策略
         */
        tpe.execute(new Task(100));

        System.out.println(tpe.getQueue());

        tpe.shutdown();
    }
}
