package tk.deriwotua.es.repositories;

import tk.deriwotua.es.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 继承ElasticSearchRepository接口
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
    ////根据标题查询
    List<Article> findByTitle(String title);
    List<Article> findByTitleOrContent(String title, String content);
    ////根据标题或内容查询(含分页)
    List<Article> findByTitleOrContent(String title, String content, Pageable pageable);
}
