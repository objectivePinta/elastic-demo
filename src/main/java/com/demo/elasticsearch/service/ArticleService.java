package com.demo.elasticsearch.service;


import com.demo.elasticsearch.model.Article;
import com.demo.elasticsearch.model.Author;
import com.demo.elasticsearch.model.Status;
import com.demo.elasticsearch.repo.ArticleRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final RestHighLevelClient restHighLevelClient;

    private final ElasticsearchOperations elasticsearchOperations;


    @Value("classpath:words.txt")
    private Resource resource;


    public ArticleService(ArticleRepository articleRepository, RestHighLevelClient restHighLevelClient, ElasticsearchOperations elasticsearchOperations) {

        this.articleRepository = articleRepository;
        this.restHighLevelClient = restHighLevelClient;
        this.elasticsearchOperations = elasticsearchOperations;
    }


    List<String> topics = List.of("tennis", "gastronomy", "agriculture");


    @SneakyThrows
    @PostConstruct
    void initialLoad() {
        List<Article> articles = getArticles();

        articles.stream().collect(Collectors.groupingBy(Article::getTopic)).forEach((s, articles1) -> {
            articleRepository.saveAll(articles1);
        });
        log.info("Indexing finished");

    }

    private List<Article> getArticles() throws IOException {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(1);
        List<Article> articles = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                .lines().map(name -> Article.builder()
                        .id(atomicInteger.getAndIncrement())
                        .numberOfWords(atomicInteger.get() * 2)
                        .lastEdited(LocalDateTime.now().minusDays(atomicInteger.get() * 2))
                        .tags(List.of("lifestyle", "automotive"))
                        .status(Status.getRandom())
                        .title("How I met " + name)
                        .topic(topics.get(topics.size() % Math.abs(new Random().nextInt(3 - 1) + 1)))
                        .authors(makeAuthors(name))
                        .build()).collect(Collectors.toList());
        return articles;
    }

    @SneakyThrows
    void initialLoadWithElasticOperations() {

        List<Article> articles = getArticles();

        articles.stream().collect(Collectors.groupingBy(Article::getTopic)).forEach((s, articles1) -> {
            elasticsearchOperations.bulkIndex(
                    articles1.stream().map(article -> new IndexQueryBuilder()
                            .withId(UUID.randomUUID().toString())
                            .withObject(article)
                            .build()).collect(Collectors.toList()), IndexCoordinates.of("blog"));


        });
        log.info("Indexing finished");

    }


    private List<Author> makeAuthors(String name) {

        return IntStream.range(0, 5).mapToObj(idx ->
                        Author.builder()
                                .firstName(idx % 2 == 0 ? name : nameInReverse(name))
                                .lastName(idx % 2 == 0 ? name : nameInReverse(name))
                                .salary(new BigDecimal(ThreadLocalRandom.current().nextDouble(999)))
                                .dateOfEmployment(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextLong(999)))
                                .build())
                .collect(Collectors.toList());

    }

    String nameInReverse(String name) {
        char[] nameArray = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = nameArray.length - 1; i >= 0; i--) {
            builder.append(nameArray[i]);
        }
        return builder.toString();
    }

}
