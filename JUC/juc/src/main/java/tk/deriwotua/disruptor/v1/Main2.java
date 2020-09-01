package tk.deriwotua.disruptor.v1;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 基于lambda表达式改进
 */
public class Main2 {
    public static void main(String[] args) {

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
         *  设置为后台线程
         *      当LongEventHandler消费者执行时在后台线程中执行
         */
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, ringBufferSize, DaemonThreadFactory.INSTANCE);

        /**
         * 指定消息处理handler
         */
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        /**
         * 生产者写法二
         */
        EventTranslator<LongEvent> translator = new EventTranslator<LongEvent>() {
            /**
             * 把值转为LongEvent
             *  这种写法与上面通过setXxx()本质没有区别
             *  这种写法主要用于迎合lambda表达式
             * @param event
             * @param sequence
             */
            @Override
            public void translateTo(LongEvent event, long sequence) {
                event.setValue(8888L);
            }
        };
        /**
         * 发布事件
         *  当产生事件后调用其translateTo方法添加队列里
         */
        ringBuffer.publishEvent(translator);
        /**
         * 生产者写法三
         */
        EventTranslatorOneArg<LongEvent, Long> translatorOneArg = new EventTranslatorOneArg<LongEvent, Long>() {
            /**
             * 把值l转为LongEvent
             * @param event
             * @param sequence 序号
             * @param l
             */
            @Override
            public void translateTo(LongEvent event, long sequence, Long l) {
                event.setValue(l);
            }
        };
        ringBuffer.publishEvent(translatorOneArg, 7777L);

        /**
         * 生产者写法四
         */
        EventTranslatorTwoArg<LongEvent, Long, Long> translatorTwoArg = new EventTranslatorTwoArg<LongEvent, Long, Long>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Long arg0, Long arg1) {
                event.setValue(arg0 + arg1);
            }
        };
        ringBuffer.publishEvent(translatorTwoArg, 10000L, 10000L);

        /**
         * 生产者写法五
         */
        EventTranslatorThreeArg<LongEvent, Long, Long, Long> translatorThreeArg = new EventTranslatorThreeArg<LongEvent,
                        Long, Long, Long>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Long arg0, Long arg1, Long arg2) {
                event.setValue(arg0 + arg1 + arg2);
            }
        };
        ringBuffer.publishEvent(translatorThreeArg, 10000L, 10000L, 10000L);

        /**
         * 生产者写法六多个参数
         */
        EventTranslatorVararg<LongEvent> translatorVararg = new EventTranslatorVararg<LongEvent>() {
            @Override
            public void translateTo(LongEvent event, long sequence, Object... args) {
                long result = 0;
                for (Object o : args){
                    result += (Long)o;
                }
                event.setValue(result);
            }
        };
        ringBuffer.publishEvent(translatorVararg, 10000L, 10000L, 10000L, 10000L);
    }
}
