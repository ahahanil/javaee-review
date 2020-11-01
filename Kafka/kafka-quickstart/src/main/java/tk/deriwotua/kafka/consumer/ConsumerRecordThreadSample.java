package tk.deriwotua.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一个Consumer对应多个partition
 *      每个partition对应一个Consumer，Consumer还有自身处理逻辑多个有点重
 *      这里常用复用形式类似NIO多路复用使用回调方式多线程处理信息
 * ![Consumer分组2](../assets/Consumer分组2.png)
 *
 * 任务中没发单独再offset处理
 */
public class ConsumerRecordThreadSample {
    private final static String TOPIC_NAME = "quickstart-topic";

    public static void main(String[] args) throws InterruptedException {
        String brokerList = "127.0.0.1:9092";
        String groupId = "test";
        int workerNum = 5;

        ConsumerExecutor consumers = new ConsumerExecutor(brokerList, groupId, TOPIC_NAME);
        consumers.execute(workerNum);

        Thread.sleep(1000000);

        consumers.shutdown();
    }

    /**
     * Consumer处理
     */
    public static class ConsumerExecutor{
        private final KafkaConsumer<String, String> consumer;
        private ExecutorService executors;

        public ConsumerExecutor(String brokerList, String groupId, String topic) {
            Properties props = new Properties();
            props.put("bootstrap.servers", brokerList);
            props.put("group.id", groupId);
            props.put("enable.auto.commit", "true");
            props.put("auto.commit.interval.ms", "1000");
            props.put("session.timeout.ms", "30000");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList(topic));
        }

        public void execute(int workerNum) {
            executors = new ThreadPoolExecutor(workerNum, workerNum, 0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(200);
                for (final ConsumerRecord record : records) {
                    /**
                     * 每条消息提交给线程池执行
                     */
                    executors.submit(new ConsumerRecordWorker(record));
                }
            }
        }

        public void shutdown() {
            if (consumer != null) {
                consumer.close();
            }
            if (executors != null) {
                executors.shutdown();
            }
            try {
                if (!executors.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Timeout.... Ignore for this case");
                }
            } catch (InterruptedException ignored) {
                System.out.println("Other thread interrupted this shutdown, ignore for this case.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 记录任务处理
     */
    public static class ConsumerRecordWorker implements Runnable {

        private ConsumerRecord<String, String> record;

        public ConsumerRecordWorker(ConsumerRecord record) {
            this.record = record;
        }

        @Override
        public void run() {
            // 假如说数据入库操作
            System.out.println("Thread - "+ Thread.currentThread().getName());
            System.err.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                    record.partition(), record.offset(), record.key(), record.value());
        }

    }
}
