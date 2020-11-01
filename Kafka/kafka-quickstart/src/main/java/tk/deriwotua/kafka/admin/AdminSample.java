package tk.deriwotua.kafka.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;

public class AdminSample {

    public final static String TOPIC_NAME="test-topic";

    /**
     * 设置AdminClient
     */
    public static AdminClient adminClient(){
        // 配置地址
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        AdminClient adminClient = AdminClient.create(properties);
        return adminClient;
    }

    public static void main(String[] args) throws Exception {
        /*AdminClient adminClient = AdminSample.adminClient();
        System.out.println("adminClient : "+ adminClient);*/
        // 创建Topic实例
        //createTopic();
        // 删除Topic实例
        //delTopics();
        // 获取Topic列表
        //topicLists();
        // 描述Topic
        describeTopics();
        // 修改Config
        //alterConfig();
        // 查询Config
        //describeConfig();
        // 增加partition数量
        //incrPartitions(2);
    }

    /**
     * 创建Topic实例
     */
    public static void createTopic() {
        AdminClient adminClient = adminClient();
        // 副本因子
        Short rs = 1;
        /**
         * 创建指定名称指定Partition个数和副本因子Topic
         */
        NewTopic newTopic = new NewTopic(TOPIC_NAME, 1 , rs);
        // 创建Topic
        CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));
        System.out.println("CreateTopicsResult : "+ topics.toString());
    }

    /**
     * 获取Topic列表
     * @throws Exception
     */
    public static void topicLists() throws Exception {
        AdminClient adminClient = adminClient();
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Set<String> names = listTopicsResult.names().get();
        Collection<TopicListing> topicListings = listTopicsResult.listings().get();
        KafkaFuture<Map<String, TopicListing>> mapKafkaFuture = listTopicsResult.namesToListings();
        // 打印names
        names.stream().forEach(System.out::println);
        // 打印topicListings
        topicListings.stream().forEach((topicList)->{
            System.out.println(topicList);
        });
        System.out.println("------------");

        // 是否查看internal选项
        ListTopicsOptions options = new ListTopicsOptions();
        // 是否携带 Internal 信息 Kafka默认的
        options.listInternal(true);
        ListTopicsResult listTopicsResultWithOption = adminClient.listTopics(options);
        names = listTopicsResultWithOption.names().get();
        topicListings = listTopicsResultWithOption.listings().get();
        mapKafkaFuture = listTopicsResultWithOption.namesToListings();
        // 打印names
        names.stream().forEach(System.out::println);
        // 打印topicListings
        topicListings.stream().forEach((topicList)->{
            System.out.println(topicList);
        });
    }

    /**
     * 删除Topic
     * @throws Exception
     */
    public static void delTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TOPIC_NAME));
        deleteTopicsResult.all().get();
    }

    /**
     * 获取Topic描述信息
     *         name ：quickstart-topic ,
     *         desc: (name=quickstart-topic,
     *             internal=false,
     *             partitions=
     *                 (partition=0,
     *                  leader=127.0.0.1:9092
     *                  (id: 0 rack: null),
     *                  replicas=127.0.0.1:9092
     *                  (id: 0 rack: null),
     *                  isr=127.0.0.1:9092
     *                  (id: 0 rack: null)),
     *                  authorizedOperations=[])
     * @throws Exception
     */
    public static void describeTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(TOPIC_NAME));
        Map<String, TopicDescription> stringTopicDescriptionMap = describeTopicsResult.all().get();
        Set<Map.Entry<String, TopicDescription>> entries = stringTopicDescriptionMap.entrySet();
        entries.stream().forEach((entry)->{
            System.out.println("name ："+entry.getKey()+" , desc: "+ entry.getValue());
        });
    }

    /**
     * 查看配置信息
     *     ConfigResource(type=TOPIC, name='quickstart-topic') ,
     *     Config(
     *         entries=[
     *          ConfigEntry(
     *            name=compression.type,
     *            value=producer,
     *            source=DEFAULT_CONFIG,
     *            isSensitive=false,
     *            isReadOnly=false,
     *            synonyms=[]),
     *          ConfigEntry(
     *             name=leader.replication.throttled.replicas,
     *             value=,
     *             source=DEFAULT_CONFIG,
     *             isSensitive=false,
     *             isReadOnly=false,
     *             synonyms=[]), ConfigEntry(name=message.downconversion.enable, value=true, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=min.insync.replicas, value=1, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=segment.jitter.ms, value=0, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=cleanup.policy, value=delete, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=flush.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=follower.replication.throttled.replicas, value=, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=segment.bytes, value=1073741824, source=STATIC_BROKER_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=retention.ms, value=604800000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=flush.messages, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=message.format.version, value=2.4-IV1, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=file.delete.delay.ms, value=60000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=max.compaction.lag.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=max.message.bytes, value=1000012, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=min.compaction.lag.ms, value=0, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=message.timestamp.type, value=CreateTime, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]),
     *          ConfigEntry(name=preallocate, value=false, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=min.cleanable.dirty.ratio, value=0.5, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=index.interval.bytes, value=4096, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=unclean.leader.election.enable, value=false, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=retention.bytes, value=-1, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=delete.retention.ms, value=86400000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=segment.ms, value=604800000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=message.timestamp.difference.max.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[]), ConfigEntry(name=segment.index.bytes, value=10485760, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[])])
     * @throws Exception
     */
    public static void describeConfig() throws Exception{
        AdminClient adminClient = adminClient();
        // TODO 这里做一个预留，集群时会讲到
        //ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, TOPIC_NAME);

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        configResourceConfigMap.entrySet().stream().forEach((entry)->{
            System.out.println("configResource : "+entry.getKey()+" , Config : "+entry.getValue());
        });
    }

    /**
     * 修改Config信息
     * @throws Exception
     */
    public static void alterConfig() throws Exception{
        AdminClient adminClient = adminClient();
        /*Map<ConfigResource,Config> configMaps = new HashMap<>();

        // 组织两个参数
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        // 标准key/value
        Config config = new Config(Arrays.asList(new ConfigEntry("preallocate","true")));
        configMaps.put(configResource,config);
        AlterConfigsResult alterConfigsResult = adminClient.alterConfigs(configMaps);*/

        /**
         * 从 2.3以上的版本新修改的API
         */
        Map<ConfigResource,Collection<AlterConfigOp>> configMaps = new HashMap<>();
        // 组织两个参数
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        AlterConfigOp alterConfigOp =
                new AlterConfigOp(new ConfigEntry("preallocate","false"),AlterConfigOp.OpType.SET);
        configMaps.put(configResource,Arrays.asList(alterConfigOp));

        AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(configMaps);
        alterConfigsResult.all().get();
    }

    /**
     * 增加partition数量
     * @param partitions
     * @throws Exception
     */
    public static void incrPartitions(int partitions) throws Exception{
        AdminClient adminClient = adminClient();
        Map<String, NewPartitions> partitionsMap = new HashMap<>();
        NewPartitions newPartitions = NewPartitions.increaseTo(partitions);
        partitionsMap.put(TOPIC_NAME, newPartitions);

        CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(partitionsMap);
        createPartitionsResult.all().get();
    }
}
