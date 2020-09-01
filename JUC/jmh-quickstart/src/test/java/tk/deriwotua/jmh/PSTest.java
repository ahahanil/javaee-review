package tk.deriwotua.jmh;

import org.openjdk.jmh.annotations.*;

/**
 * JMH基础测试
 */
public class PSTest {
    /**
     * 基础测试
     */
    @Benchmark
    /**
     * Warmup 预热，由于JVM中对于特定代码会存在优化（JIT本地化），预热对于测试结果很重要
     *  启动后先调用预热一次然后等待3s
     */
    @Warmup(iterations = 1, time = 3)
    /**
     * 启动多少个线程去执行
     */
    @Fork(5)
    /**
     * 基础测试模式
     *  Throughput 测试吞吐量
     */
    @BenchmarkMode(Mode.Throughput)
    /**
     * 方法测试多少遍以取平均值
     */
    @Measurement(iterations = 1, time = 3)
    public void testForEach() {
        /**
         * 测试方法每秒能执行多少次
         */
        PS.foreach();
    }
}
