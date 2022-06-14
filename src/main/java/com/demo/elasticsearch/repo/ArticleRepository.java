package com.demo.elasticsearch.repo;

import com.demo.elasticsearch.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    Page<Article> findByAuthorsLastName(String lastName, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.lastName\": \"?0\"}}]}}")
    Page<Article> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
