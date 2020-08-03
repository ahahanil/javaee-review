package tk.deriwotua;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import tk.deriwotua.es.Article;

import java.net.InetAddress;

public class ElasticSearchClientTest {

    private TransportClient client;

    @Before
    public void init() throws Exception {
        //创建一个Settings对象
        Settings settings = Settings.builder()
                .put("cluster.name", "es-cluster")
                .build();
        //创建一个TransPortClient对象
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9303));

    }

    @Test
    public void createIndex() throws Exception {
        //1、创建一个Settings对象，相当于是一个配置信息。主要配置集群的名称。
        Settings settings = Settings.builder()
                .put("cluster.name", "es-cluster")
                .build();
        //2、创建一个客户端Client对象
        TransportClient client = new PreBuiltTransportClient(settings);
        // 客户端添加TCP节点地址
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9303));
        //3、使用client对象创建一个索引库
        client.admin().indices().prepareCreate("index_hello")
                //执行操作
                .get();
        //4、关闭client对象
        client.close();
    }

    @Test
    public void setMappings() throws Exception {
        //创建一个Settings对象
        Settings settings = Settings.builder()
                .put("cluster.name", "es-cluster")
                .build();
        //创建一个TransPortClient对象
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9302))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9303));

        /*{
            "article":{
                "properties":{
                    "id":{
                        "type":"long",
                        "store":true
                    },
                    "title":{
                        "type":"text",
                         "store":true,
                         "index":true,
                         "analyzer":"ik_smart"
                    },
                    "content":{
                        "type":"text",
                         "store":true,
                         "index":true,
                         "analyzer":"ik_smart"
                    }
                }
            }
        }*/
        //创建一个Mappings信息
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                    // type
                    .startObject("article")
                        // document
                        .startObject("properties")
                            // field
                            .startObject("id")
                                .field("type","long")
                                .field("store", true)
                            .endObject()
                            .startObject("title")
                                .field("type", "text")
                                .field("store", true)
                                .field("analyzer", "ik_smart")
                            .endObject()
                            .startObject("content")
                                .field("type", "text")
                                .field("store", true)
                                .field("analyzer","ik_smart")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
        //使用client把mapping信息设置到索引库中
        //方式一
        /*client.admin().indices()
                //设置要做映射的索引
                .preparePutMapping("index_hello")
                //设置要做映射的type
                .setType("article")
                //mapping信息，可以是XContentBuilder对象可以是json格式的字符串
                .setSource(xContentBuilder)
                //执行操作
                .get();*/
        // 方式二
        PutMappingRequest mapping = Requests.putMappingRequest("index_hello")
                .type("article").source(xContentBuilder);
        client.admin().indices().putMapping(mapping).get();
        //关闭链接
        client.close();
    }

    @Test
    public void testAddDocument() throws Exception {
        //创建一个client对象
        //创建一个文档对象
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("id",2l)
                    .field("title","北方入秋速度明显加快 多地降温幅度最多可达10度22222")
                    .field("content", "阿联酋一架客机在纽约机场被隔离 10名乘客病倒")
                .endObject();
        //把文档对象添加到索引库
        // 方式一
        /*client.prepareIndex()
                //设置索引名称
                .setIndex("index_hello")
                //设置type
                .setType("article")
                //设置文档的id，如果不设置的话自动的生成一个id
                .setId("2")
                //设置文档信息
                .setSource(xContentBuilder)
                //执行操作
                .get();*/
        /**
         * 方式二
         * 参数一blog1：表示索引对象
         * 参数二article：类型
         * 参数三1：建立id
         */
        client.prepareIndex("index_hello", "article", "1").setSource(xContentBuilder).get();
        //关闭客户端
        client.close();
    }

    @Test
    public void testAddDocument2() throws Exception {
        //创建一个Article对象
        Article article = new Article();
        //设置对象的属性
        article.setId(3l);
        article.setTitle("MH370坠毁在柬埔寨密林?中国一公司调十颗卫星去拍摄");
        article.setContent("警惕荒唐的死亡游戏!俄15岁少年输掉游戏后用电锯自杀");
        //把article对象转换成json格式的字符串。
        String jsonDocument = JSONObject.toJSONString(article);
        System.out.println(jsonDocument);
        //使用client对象把文档写入索引库
        client.prepareIndex("index_hello","article", "3")
                .setSource(jsonDocument, XContentType.JSON).get();
        client.prepareIndex("index_hello","article", "6")
                .setSource(jsonDocument.getBytes(), XContentType.JSON).get();
        //下面两个添加方法已标注过期
        client.prepareIndex("index_hello","article", "4")
                .setSource(jsonDocument).get();
        client.prepareIndex("index_hello","article", "5")
                .setSource(jsonDocument.getBytes()).get();

        //关闭客户端
        client.close();
    }

    @Test
    public void testAddDocument3() throws Exception {
        for (int i = 7; i < 100; i++) {
            //创建一个Article对象
            Article article = new Article();
            //设置对象的属性
            article.setId(i);
            article.setTitle("女护士路遇昏迷男子跪地抢救：救人是职责更是本能" + i);
            article.setContent("江西变质营养餐事件已致24人就医 多名官员被调查" + i);
            //把article对象转换成json格式的字符串。
            String jsonDocument = JSONObject.toJSONString(article);
            System.out.println(jsonDocument);
            //使用client对象把文档写入索引库
            client.prepareIndex("index_hello","article", i + "")
                    .setSource(jsonDocument, XContentType.JSON).get();

        }
        //关闭客户端
        client.close();
    }
}
