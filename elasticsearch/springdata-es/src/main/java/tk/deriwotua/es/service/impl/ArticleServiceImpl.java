package tk.deriwotua.es.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.deriwotua.es.entity.Article;
import tk.deriwotua.es.repositories.ArticleRepository;
import tk.deriwotua.es.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public void save(Article article) {
        articleRepository.save(article);
    }

}
