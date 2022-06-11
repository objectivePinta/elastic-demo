package com.demo.elasticsearch;


import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LogDataAccessorService {


  private ElasticsearchRestTemplate elasticsearchRestTemplate;


  public List createLogData (final List<LogData> logDataList) {

    List queries = logDataList.stream()
        .map(logData ->
                 new IndexQueryBuilder()
                     .withId(logData.getId())
                     .withObject(logData).build())
        .collect(Collectors.toList());
    ;

    return elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of("logdataindex"));
  }


  public List<LogData> getLogDatasByHost (String host) {

    SearchHits<LogData> searchHits = elasticsearchRestTemplate.search(new NativeSearchQueryBuilder()
                                                                          .withQuery(QueryBuilders.matchQuery(
                                                                              "host",
                                                                              host
                                                                          ))
                                                                          .build(), LogData.class);

    return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
  }

  @PostConstruct
  void saveAndRetrieve() {
    List data = List.of(LogData.builder().date(new Date()).message("Yes").host("123").size(12.3d).id(UUID.randomUUID().toString())
                .status("UNREAD").build());
    createLogData(data);
    getLogDatasByHost("123").forEach(System.out::println);
  }
}
