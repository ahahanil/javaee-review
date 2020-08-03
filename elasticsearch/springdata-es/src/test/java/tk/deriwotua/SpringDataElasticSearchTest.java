package tk.deriwotua;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tk.deriwotua.es.entity.Article;
import tk.deriwotua.es.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataElasticSearchTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TransportClient client;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**创建索引和映射*/
    @Test
    public void createIndex() throws Exception {
        //创建索引，并配置映射关系
        elasticsearchTemplate.createIndex(Article.class);
        //配置映射关系
        //elasticsearchTemplate.putMapping(Article.class);
    }

    /**测试保存文档*/
    @Test
    public void addDocument() throws Exception {
        Article article = new Article();
        article.setId(100L);
        article.setTitle("测试SpringData ElasticSearch");
        article.setContent("Spring Data ElasticSearch 基于 spring data API 简化 elasticSearch操作，将原始操作elasticSearch的客户端API 进行封装 \n" +
                "    Spring Data为Elasticsearch Elasticsearch项目提供集成搜索引擎");
        articleRepository.save(article);
    }


    @Test
    public void addDocumentBatch() throws Exception {
        for (long i = 10; i <= 20; i++) {
            //创建一个Article对象
            Article article = new Article();
            article.setId(i);
            article.setTitle("【图解】测试SpringData ElasticSearch" + i);
            article.setContent("盛会再携手—各国政要高度评价东博会和商务与投资峰会");
            //把文档写入索引库
            articleRepository.save(article);
        }
    }

    /**测试更新*/
    @Test
    public void update(){
        Article article = new Article();
        article.setId(1001L);
        article.setTitle("elasticSearch 3.0版本发布...更新");
        article.setContent("ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");
        articleRepository.save(article);
    }

    /**测试删除*/
    @Test
    public void delete(){
        Article article = new Article();
        article.setId(1001L);
        articleRepository.delete(article);
    }

    @Test
    public void deleteDocumentById() throws Exception {
        articleRepository.deleteById(1l);
        //全部删除
        //articleRepository.deleteAll();
    }

    @Test
    public void findAll() throws Exception {
        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(a -> System.out.println(a));
    }

    /**分页查询*/
    @Test
    public void findAllPage(){
        Pageable pageable = PageRequest.of(1,10);
        Page<Article> page = articleRepository.findAll(pageable);
        for(Article article:page.getContent()){
            System.out.println(article);
        }
    }

    @Test
    public void testFindById() throws Exception {
        Optional<Article> optional = articleRepository.findById(1l);
        Article article = optional.get();
        System.out.println(article);
    }

    /**条件查询*/
    @Test
    public void testFindByTitle() throws Exception {
        List<Article> list = articleRepository.findByTitle("maven是一个工程构建工具");
        list.stream().forEach(a -> System.out.println(a));
    }

    /**条件分页查询*/
    @Test
    public void testFindByTitleOrContent() throws Exception {
        Pageable pageable = PageRequest.of(1, 15);
        articleRepository.findByTitleOrContent("maven", "商务与投资", pageable)
                .forEach(a -> System.out.println(a));
    }

    @Test
    public void testNativeSearchQuery() throws Exception {
        // 创建一个SearchQuery对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                // 设置查询条件，此处可以使用QueryBuilders创建多种查询
                .withQuery(QueryBuilders.queryStringQuery("maven是一个工程构建工具").defaultField("title"))
                // 设置分页信息
                .withPageable(PageRequest.of(0, 15))
                // 创建SearchQuery对象
                .build();
        // 使用模板对象执行查询
        List<Article> articleList = elasticsearchTemplate.queryForList(query, Article.class);
        articleList.forEach(a -> System.out.println(a));
    }

}
