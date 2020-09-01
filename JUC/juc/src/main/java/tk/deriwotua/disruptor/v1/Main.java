package tk.deriwotua.disruptor.v1;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        //Executor executor = Executors.newCachedThreadPool();

        /**
         * 工厂类
         */
        LongEventFactory factory = new LongEventFactory();

        /**
         * 环形队列长度
         * must be power of 2
         */
        int ringBufferSize = 1024;

        /**
         * 创建 Disruptor
         *  需要设置队列元素工厂类
         *  需要设置队列长度
         *  需要设置线程工厂
         *      当LongEventHandler消费者执行时需要在线程工厂创建子线程中执行
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, ringBufferSize, Executors.defaultThreadFactory());

        /**
         * 指定消息处理handler
         */
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动
        disruptor.start();

        /**
         * 生产者
         */
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        // 获取下一个可用的位置(初始值为0)
        long sequence = ringBuffer.next();
        try {
            // 直接获取指定位置的事件
            LongEvent longEvent = ringBuffer.get(sequence);
            // 由于disruptor在初始化时直接向队列每个位置都预置LongEvent所以这里不需要再判空
            // 且插入时也不需要new创建操作直接setXxx()赋值 效率高
            longEvent.setValue(8888L);
        } finally {
            /**
             * 发布等待被消费
             */
            ringBuffer.publish(sequence);
        }


        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);

        for(long l = 0; l<100; l++) {
            bb.putLong(0, l);

            producer.onData(bb);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        disruptor.shutdown();
    }
}
