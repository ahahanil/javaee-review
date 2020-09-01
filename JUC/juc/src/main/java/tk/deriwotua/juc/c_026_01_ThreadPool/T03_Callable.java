/**
 * 认识Callable，对Runnable进行了扩展
 * 对Callable的调用，可以有返回值
 */
package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

/**
 * Callable 接口对Runnable进行了扩展
 *  Callable#call() 相当于 Runnable#run()
 *  且Callable#call()有返回值
 */
public class T03_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * 返回值String类型 Callable任务
         */
        Callable<String> c = new Callable() {
            @Override
            public String call() throws Exception {
                return "Hello Callable";
            }
        };

        ExecutorService service = Executors.newCachedThreadPool();
        /**
         * 任务提交给线程池(异步)
         */
        Future<String> future = service.submit(c);
        /**
         * java.util.concurrent.Future#get() 阻塞式等待异步操作直到有结果产生
         */
        System.out.println(future.get());

        /**
         * 上述过程 定义一个返回值是String类型的Callable任务提交给线程池异步执行
         * 由于不知道线程池什么时候执行产生结果使用java.util.concurrent.Future#get() 阻塞式等待异步操作直到有结果产生
         * 直到有结果产生后放在 Future<String> 里
         */

        // 关闭线程池
        service.shutdown();
    }

}
