package tk.deriwotua.disruptor.v1;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

/**
 * lambda 简写
 */
public class Main3 {
    public static void main(String[] args) {

        /**
         * 环形队列长度
         * must be power of 2
         */
        int ringBufferSize = 1024;

        /**
         * 创建 Disruptor
         *  通过lambda表达式写法
         *  需要设置队列长度
         *  需要设置线程工厂
         *      当LongEventHandler消费者执行时需要在线程工厂创建子线程中执行
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, ringBufferSize, Executors.defaultThreadFactory());

        /**
         * 指定消息者处理handler
         */
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("event: " + event));
        // 启动
        disruptor.start();

        /**
         * 生产者
         */
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ringBuffer.publishEvent((event, sequence) -> event.setValue(10000L));
        ringBuffer.publishEvent((event, sequence, l) -> event.setValue(l), 10000L);
        ringBuffer.publishEvent((event, sequence, arg0, arg1) -> event.setValue(arg0 + arg1), 10000L, 10000L);

        disruptor.shutdown();
    }
}
