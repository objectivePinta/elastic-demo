package com.demo.elasticsearch.service;


import com.demo.elasticsearch.model.Article;
import com.demo.elasticsearch.model.Author;
import com.demo.elasticsearch.repo.ArticleRepository;
import lombok.SneakyThrows;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  private final RestHighLevelClient restHighLevelClient;


  @Value("classpath:words.txt")
  private Resource resource;


  public ArticleService (ArticleRepository articleRepository, RestHighLevelClient restHighLevelClient) {

    this.articleRepository = articleRepository;
    this.restHighLevelClient = restHighLevelClient;
  }


  List<String> topics = List.of("tennis", "gastronomy", "agriculture");


  @SneakyThrows
  void initialLoad () {

    new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        .lines().forEach(name -> {

          Article article = Article.builder()
              .id(UUID.randomUUID().toString())
              .title("How I met " + name)
              .topic(topics.get(topics.size() % Math.abs(new Random().nextInt(3))))
              .authors(makeAuthors(name))
              .build();
          articleRepository.save(article);
        });
  }


  private List<Author> makeAuthors (String name) {

    return IntStream.range(0, 10).mapToObj(idx ->
                                               Author.builder()
                                                   .firstName(name + name)
                                                   .lastName(idx + name + name)
                                                   .build())
        .collect(Collectors.toList());

  }


  @SneakyThrows
  public void indexAuthors () {

    new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        .lines().map(name -> {

          Article article = Article.builder()
              .id(UUID.randomUUID().toString())
              .title("How I met " + name)
              .authors(makeAuthors(name))
              .build();
          return article;
        }).collect(Collectors.toSet());
  }


}
