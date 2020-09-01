package tk.deriwotua.disruptor.v1;

import com.lmax.disruptor.EventFactory;

/**
 * Event工厂类生产Event
 *  disruptor工厂类需要实现EventFactory接口newInstance方法
 *  为什么要通过工厂类创建Event?
 *   这里牵扯到效率问题：disruptor初始化的时候，会调用Event工厂，对ringBuffer进行内存的提前分配
 *   因此GC产频率会降低
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    /**
     * 创建Event
     *  disruptor初始化时会调用直接创建对象放到环形队列里(内存提前分配)
     *  然后当插入时直接赋值替代new创建来提高效率
     * @return
     */
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
