package tk.deriwotua.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class StreamSample {
    /**
     * 输入Topic
     */
    private static final String INPUT_TOPIC = "quickstart-stream-in";
    /**
     * 输出Topic
     */
    private static final String OUT_TOPIC = "quickstart-stream-out";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定Stream application ID
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        // 序列化相关
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 构建流结构拓扑
        final StreamsBuilder builder = new StreamsBuilder();
        // 构建word-count
        //wordCountStream(builder);
        // 构建foreachStream
        foreachStream(builder);
        // stream 流
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        // 启动
        streams.start();
    }

    /**
     * 定义流计算过程
     * @param builder
     */
    static void foreachStream(final StreamsBuilder builder) {
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
        source
                /**
                 * 按空格拆分
                 */
                .flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                // 循环打印
                .foreach((key, value) -> System.out.println(key + " : " + value));
    }

    /**
     * 定义流计算过程
     * @param builder
     */
    static void wordCountStream(final StreamsBuilder builder) {
        // 不断从输入INPUT_TOPIC上获取新数据，并且追加到流上的一个抽象对象
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
        /**
         * 接收输入：Hello World
         *  统计每个单词个数
         * KTable是数据集合的抽象对象
         * （算子）
         */
        final KTable<String, Long> count =
                source
                        /**
                         * flatMapValues -> 将一行数据拆分为多行数据  key 1 , value Hello World
                         * flatMapValues -> 将一行数据拆分为多行数据  key 1 , value Hello key xx , value World
                         *
                         * key 1 , value Hello   -> Hello 1  World 2
                         * key 2 , value World
                         * key 3 , value World
                         *
                         * 这里按空格拆分
                         */
                        .flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                        // 合并 -> 按value值合并
                        .groupBy((key, value) -> value)
                        // 统计出现的总数
                        .count();

        // 将结果输出到OUT_TOPIC中
        count.toStream().to(OUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

}
