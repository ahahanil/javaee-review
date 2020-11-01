package tk.deriwotua.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class ConsumerSample {
    private final static String TOPIC_NAME = "quickstart-topic";


    public static Properties properties() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // 消费分组
        props.setProperty("group.id", "test");
        // 自动提交
        props.setProperty("enable.auto.commit", "true");
        // 提交间隔
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

    /**
     * 简单拉取消息(自动提交)
     *  真实场景获取到的每条消息都需要进行后续处理
     *  业务处理就存在失败、耗时等问题
     */
    private static void helloWorld() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties());
        // 消费订阅哪一个Topic或者几个Topic
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            // 每秒拉取一次
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value());
        }
    }

    /**
     * 手动提交offset
     */
    private static void commitOffset() {
        Properties properties = properties();
        // 手动提交
        properties.setProperty("enable.auto.commit", "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties);
        // 消费订阅哪一个Topic或者几个Topic
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            // 每秒拉取一次
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            for (ConsumerRecord<String, String> record : records) {
                // 想把数据保存到数据库，成功就成功，不成功...
                // TODO record 2 db
                System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                        record.partition(), record.offset(), record.key(), record.value());
                // 如果失败，则回滚， 不要提交offset
            }

            // 如果成功，手动通知offset提交
            consumer.commitAsync();
        }
    }

    /**
     * 手动提交offset,并且手动控制partition
     */
    private static void commitOffsetWithPartition() {
        Properties properties = properties();
        // 手动提交
        properties.setProperty("enable.auto.commit", "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties);
        // 消费订阅哪一个Topic或者几个Topic
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        while (true) {
            /**
             * 拉取的数据包含多个partition
             *  处理时倾向于一个partition一个partition处理
             *  某个partition处理失败了后面只针对失败的partition再次处理
             */
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            // 每个partition单独处理
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> pRecord = records.records(partition);
                for (ConsumerRecord<String, String> record : pRecord) {
                    System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                            record.partition(), record.offset(), record.key(), record.value());

                }
                long lastOffset = pRecord.get(pRecord.size() - 1).offset();
                // 单个partition中的offset，并且进行提交
                Map<TopicPartition, OffsetAndMetadata> offset = new HashMap<>();
                // 每次获取下一个offset
                offset.put(partition, new OffsetAndMetadata(lastOffset + 1));
                // 提交offset
                consumer.commitSync(offset);
                System.out.println("=============partition - " + partition + " end================");
            }
        }
    }

    /**
     * 手动提交offset,并且手动控制partition(不再订阅Topic而是订阅某个Topic的某个分区)
     */
    private static void commitOffsetWithPartition2() {
        Properties properties = properties();
        // 手动提交
        properties.setProperty("enable.auto.commit", "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties);

        // quickstart-topic - 0, quickstart-topic - 1两个partition
        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition p1 = new TopicPartition(TOPIC_NAME, 1);

        // 消费订阅哪一个Topic或者几个Topic
        //consumer.subscribe(Arrays.asList(TOPIC_NAME));

        /**
         * 不再订阅Topic而是订阅某个Topic的某个分区
         */
        consumer.assign(Arrays.asList(p0));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            // 每个partition单独处理
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> pRecord = records.records(partition);
                for (ConsumerRecord<String, String> record : pRecord) {
                    System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                long lastOffset = pRecord.get(pRecord.size() - 1).offset();
                // 单个partition中的offset，并且进行提交
                Map<TopicPartition, OffsetAndMetadata> offset = new HashMap<>();
                offset.put(partition, new OffsetAndMetadata(lastOffset + 1));
                // 提交offset
                consumer.commitSync(offset);
                System.out.println("=============partition - " + partition + " end================");
            }
        }
    }

    /**
     * 手动指定offset的起始位置，及手动提交offset
     */
    private static void controlOffset() {
        Properties properties = properties();
        // 手动提交
        properties.setProperty("enable.auto.commit", "false");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties);
        // quickstart-topic - 0,1两个partition
        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);

        // 消费订阅某个Topic的某个分区
        consumer.assign(Arrays.asList(p0));

        while (true) {
            /**
             * 手动指定offset起始位置两种情况
             * 1、人为控制offset起始位置
             * 2、如果出现程序错误，重复消费一次
             */
            /**
             * 1、第一次从0消费【一般情况】
             * 2、比如一次消费了100条， offset置为101并且存入Redis(存储下一次要消费的起始位置)
             * 3、每次poll之前，从redis中获取最新的offset位置
             * 4、每次从这个位置开始消费
             */
            consumer.seek(p0, 700);

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            // 每个partition单独处理
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> pRecord = records.records(partition);
                for (ConsumerRecord<String, String> record : pRecord) {
                    System.err.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                long lastOffset = pRecord.get(pRecord.size() - 1).offset();
                // 单个partition中的offset，并且进行提交
                Map<TopicPartition, OffsetAndMetadata> offset = new HashMap<>();
                offset.put(partition, new OffsetAndMetadata(lastOffset + 1));
                // 提交offset
                consumer.commitSync(offset);
                System.out.println("=============partition - " + partition + " end================");
            }
        }
    }

    public static void main(String[] args) {
        // 自动提交
        //helloWorld();
        // 手动提交offset
        //commitOffset();
        // 手动对每个Partition进行提交
        //commitOffsetWithPartition();
        // 手动订阅某个或某些分区，并提交offset
        //commitOffsetWithPartition2();
        // 手动指定offset的起始位置，及手动提交offset
        //controlOffset();
        // 流量控制
        controlPause();
    }

    /**
     * 流量控制 - 限流
     */
    private static void controlPause() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties());

        // quickstart-topic - 0,1两个partition
        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition p1 = new TopicPartition(TOPIC_NAME, 1);

        // 消费订阅某个Topic的某个分区
        consumer.assign(Arrays.asList(p0, p1));
        long totalNum = 40;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            // 每个partition单独处理
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> pRecord = records.records(partition);
                // 计数
                long num = 0;
                for (ConsumerRecord<String, String> record : pRecord) {
                    System.out.printf("partition = %d , offset = %d, key = %s, value = %s%n",
                            record.partition(), record.offset(), record.key(), record.value());
                    /**
                     * 限流逻辑
                     * 1、接收到record信息以后，去令牌桶中拿取令牌
                     * 2、如果获取到令牌，则继续业务处理
                     * 3、如果获取不到令牌， 则pause等待令牌
                     * 4、当令牌桶中的令牌足够， 则将consumer置为resume状态
                     */
                    num++;
                    if (record.partition() == 0) {
                        if (num >= totalNum) {
                            /**
                             * 暂停
                             */
                            consumer.pause(Arrays.asList(p0));
                        }
                    }

                    if (record.partition() == 1) {
                        if (num == 40) {
                            /**
                             * 取消暂停
                             */
                            consumer.resume(Arrays.asList(p0));
                        }
                    }
                }

                long lastOffset = pRecord.get(pRecord.size() - 1).offset();
                // 单个partition中的offset，并且进行提交
                Map<TopicPartition, OffsetAndMetadata> offset = new HashMap<>();
                offset.put(partition, new OffsetAndMetadata(lastOffset + 1));
                // 提交offset
                consumer.commitSync(offset);
                System.out.println("=============partition - " + partition + " end================");
            }
        }
    }

}
