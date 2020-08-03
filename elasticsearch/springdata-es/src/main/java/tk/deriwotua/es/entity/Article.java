package tk.deriwotua.es.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 通过注解 JavaBean（pojo）映射到一个Document上
 *
 * @Document 文档对象 （索引信息、文档类型 ）
 *  @Document(indexName="index_spring_data", type="article")：
 *      indexName：索引的名称（必填项）
 *      type：索引的类型
 * @Id 文档主键 唯一标识
 * @Field 每个文档的字段配置（类型、是否分词、是否存储、分词器 ）
 *  @Field(index=true, analyzer="ik_smart", store=true, searchAnalyzer="ik_smart", type = FieldType.text)
 *      index：是否设置分词
 *      analyzer：存储时使用的分词器
 *      store：是否存储
 *      searchAnalyze：搜索时使用的分词器
 *      type: 数据类型
 */
@Document(indexName = "index_spring_data", type = "article")
public class Article {
    //@Id 文档主键 唯一标识
    @Id
    //@Field 每个文档的字段配置（类型、是否分词、是否存储、分词器 ）
    @Field(store = true, index = false, type = FieldType.Long)
    private Long id;
    @Field(index = true, analyzer = "ik_smart", store = true, searchAnalyzer = "ik_smart", type = FieldType.text)
    private String title;
    @Field(index = true, analyzer = "ik_smart", store = true, searchAnalyzer = "ik_smart", type = FieldType.text)
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
