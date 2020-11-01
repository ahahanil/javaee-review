package tk.deriwotua.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerSample {

    private final static String TOPIC_NAME = "jiangzh-topic";

    public static Properties properties(){
        Properties properties = new Properties();
        // 用于建立与 kafka 集群连接的 host/port 组
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        /**
         * producer 需要 server 接收到数据之后发出的确认接收的信号，此项配置就是指 procuder 需要多少个这样的确认信号。
         *  ACK值
         *      0
         *      1
         *      all
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 设置大于 0 的值将使客户端重新发送任何数据，一旦这些数据发送失败
        properties.put(ProducerConfig.RETRIES_CONFIG, "0");
        // 批次大小,以减少请求次数
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        // 多长时间发送一个批次
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        // 缓存最大值
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // 序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    /**
     * Producer异步发送演示
     */
    public static void producerSend() {
        /**
         * Producer的主对象
         *  MetricConfig
         *  加载负载均衡器
         *  初始化Serializer
         *  初始化RecordAccumulator（类似计数器）
         *  启动newSender守护线程
         * Producer是线程安全的
         * Producer并不是接到一条发一条而是批次批量发送
         */
        Producer<String, String> producer = new KafkaProducer<>(properties());
        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);
            /**
             * 发送时会先计算分区消息具体进入哪一个partition
             * 计算批次 accumulator
             * 发送主要干的是创建批次、向批次中追加消息，然后由守护线程发送
             */
            producer.send(record);
        }
        // 所有的通道打开都需要关闭
        producer.close();
    }

    /**
     * Producer异步阻塞发送
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void producerSyncSend() throws ExecutionException, InterruptedException {
        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties());

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            String key = "key-" + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, key, "value-" + i);

            Future<RecordMetadata> send = producer.send(record);
            RecordMetadata recordMetadata = send.get();
            System.out.println(key + "partition : " + recordMetadata.partition() + " , offset : " + recordMetadata.offset());
        }

        // 所有的通道打开都需要关闭
        producer.close();
    }

    /**
     * Producer异步发送带回调函数
     */
    public static void producerSendWithCallback() {
        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties());

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println(
                            "partition : " + recordMetadata.partition() + " , offset : " + recordMetadata.offset());
                }
            });
        }

        // 所有的通道打开都需要关闭
        producer.close();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Producer异步发送演示
        //producerSend();
        // Producer异步阻塞发送演示
        //producerSyncSend();
        // Producer异步发送带回调函数
        //producerSendWithCallback();
        // Producer异步发送带回调函数和Partition负载均衡
        producerSendWithCallbackAndPartition();
    }

    /**
     * Producer异步发送带回调函数和自定义Partition负载均衡
     */
    public static void producerSendWithCallbackAndPartition() {
        Properties properties = properties();
        // 设置自定义分区
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "tk.deriwotua.kafka.producer.SamplePartition");

        // Producer的主对象
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // 消息对象 - ProducerRecord
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, "key-" + i, "value-" + i);

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println(
                            "partition : " + recordMetadata.partition() + " , offset : " + recordMetadata.offset());
                }
            });
        }

        // 所有的通道打开都需要关闭
        producer.close();
    }

}
