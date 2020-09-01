package tk.deriwotua.disruptor.v1;

import com.lmax.disruptor.EventHandler;

/**
 * 消息的消费者
 *  disruptor消费者需要实现EventHandler的onEvent()方法
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    public static long count = 0L;

    /**
     *  记录下当前线程处理了多个个event
     * @param longEvent 事件(环形队列里的元素)
     * @param sequence  RingBuffer的序号
     * @param endOfBatch 是否为最后一个元素
     * @throws Exception
     */
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        count++;
        System.out.println("[" + Thread.currentThread().getName() + "] " + longEvent + " 序号：" + sequence);
    }
}
