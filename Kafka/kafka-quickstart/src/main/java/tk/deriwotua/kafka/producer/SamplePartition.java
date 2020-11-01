package tk.deriwotua.kafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区
 */
public class SamplePartition implements Partitioner {
    /**
     * 处理什么样的数据进入哪个partition
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        /**
         * 业务中key格式
         *      key-1
         *      key-2
         *      key-3
         */
        String keyStr = key + "";
        String keyInt = keyStr.substring(4);
        System.out.println("keyStr : "+keyStr + "keyInt : "+keyInt);

        int i = Integer.parseInt(keyInt);
        // 取余
        return i%2;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
