package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

/**
 * 自定义拒绝策略
 */
public class T14_MyRejectedHandler {
    public static void main(String[] args) {
        ExecutorService service = new ThreadPoolExecutor(4, 4,
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(6),
                Executors.defaultThreadFactory(),
                new MyHandler());
    }

    /**
     * 自定义拒绝策略 继承 RejectedExecutionHandler
     */
    static class MyHandler implements RejectedExecutionHandler {
        /**
         * 超负荷后回调方法
         * @param r
         * @param executor
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //log("r rejected")
            //save r kafka mysql redis
            //try 3 times
            if(executor.getQueue().size() < 10000) {
                //try put again();
            }
        }
    }
}
