/**
 * 认识ExecutorService,阅读API文档
 * 认识submit方法，扩展了execute方法，具有一个返回值
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecutorService 除了完善 Executor 任务执行生命周期
 *  扩展了execute方法
 *      submit() 提交任务什么时候执行由线程池决定(相当于异步)
 *      然后产生一个结果
 */
public class T02_ExecutorService  {
    public static void main(String[] args) {
        ExecutorService e = Executors.newCachedThreadPool();
    }
}
