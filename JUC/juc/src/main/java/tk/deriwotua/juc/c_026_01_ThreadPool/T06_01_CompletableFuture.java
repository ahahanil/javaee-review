/**
 * 假设你能够提供一个服务
 * 这个服务查询各大电商网站同一类产品的价格并汇总展示
 * @author 马士兵 http://mashibing.com
 */

package tk.deriwotua.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture 任务管理
 *  底层 fork-join-pool
 *  管理多个任务产生的Future结果(对结果进行组合等)
 *
 * CompletableFuture 实现比价
 */
public class T06_01_CompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start, end;

        /*start = System.currentTimeMillis();
        // 从数据源中取出数据进行比价
        priceOfTM();
        priceOfTB();
        priceOfJD();

        end = System.currentTimeMillis();
        System.out.println("use serial method call! " + (end - start));*/

        start = System.currentTimeMillis();
        /**
         * 上面单线程顺序调用从数据源取数据效率低
         * 下面通过 CompletableFuture 多线程并行从数据源取数据
         * 使用CompletableFuture#supplyAsync()方法异步执行三个任务从各数据源加载数据产生结果放在CompletableFuture
         */
        CompletableFuture<Double> futureTM = CompletableFuture.supplyAsync(()->priceOfTM());
        CompletableFuture<Double> futureTB = CompletableFuture.supplyAsync(()->priceOfTB());
        CompletableFuture<Double> futureJD = CompletableFuture.supplyAsync(()->priceOfJD());
        /**
         * 当上面三个任务都产生结果后
         * 通常异步提交后返回Future任何通过Future#get()阻塞等待结果
         *  那么有几个任务就需要几个相对应Future#get()阻塞等待结果
         * 而通过 CompletableFuture 可以把任务添加到一个CompletableFuture进行管理
         * 即可进行对一堆任务进行管理
         */
        CompletableFuture.allOf(futureTM, futureTB, futureJD).join();
        /**
         * 任何一个任务完成
         */
        //CompletableFuture.anyOf(futureTM, futureTB, futureJD).join();

        /**
         * CompletableFuture.supplyAsync() 有多种写法
         *  流式分步处理
         */
        CompletableFuture.supplyAsync(()->priceOfTM())
                // 从数据源加载数据后把产生的结果转成String类型
                .thenApply(String::valueOf)
                // 然后再拼接一个前缀
                .thenApply(str-> "price " + str)
                // 最后打印
                .thenAccept(System.out::println);

        end = System.currentTimeMillis();
        System.out.println("use completable future! " + (end - start));

        /**
         * 上面都是异步执行阻塞等待 操作执行完
         */
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据源1
     * @return
     */
    private static double priceOfTM() {
        delay();
        return 1.00;
    }
    /**
     * 数据源2
     * @return
     */
    private static double priceOfTB() {
        delay();
        return 2.00;
    }
    /**
     * 数据源3
     * @return
     */
    private static double priceOfJD() {
        delay();
        return 3.00;
    }

    /*private static double priceOfAmazon() {
        delay();
        throw new RuntimeException("product not exist!");
    }*/

    /**
     * 模拟从数据源取数据过程耗时逻辑
     */
    private static void delay() {
        int time = new Random().nextInt(500);
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("After %s sleep!\n", time);
    }
}
