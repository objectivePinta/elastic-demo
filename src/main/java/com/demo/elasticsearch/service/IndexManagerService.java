package com.demo.elasticsearch.service;


import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
public class IndexManagerService {

  private final RestHighLevelClient openSearchClient;


  @SneakyThrows
  public void createIndex () {

    CreateIndexRequest createIndexRequest = new CreateIndexRequest("cats");

    createIndexRequest.settings(Settings.builder() //Specify in the settings how many shards you want in the index.
                                    .put("index.number_of_shards", 3)
                                    .put("index.number_of_replicas", 1)
    );

    Map<String, Object> message = new HashMap<>();
    message.put("type", "text");
    Map<String, Object> properties = new HashMap<>();
    properties.put("message", message);
    Map<String, Object> mapping = new HashMap<>();
    mapping.put("properties", properties);
    createIndexRequest.mapping(mapping);

    createIndexRequest.mapping(mapping);
    boolean created = openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
    log.info("Index created:" + created);
  }


  @SneakyThrows
  @PostConstruct
  public void createMappingsFromFile () {

    URL url = Resources.getResource("mappings.json");
    String data = Resources.toString(url, Charsets.UTF_8);
    CreateIndexRequest createIndexRequest = new CreateIndexRequest("cats");
    createIndexRequest.settings(Settings.builder() //Specify in the settings how many shards you want in the index.
                                    .put("index.number_of_shards", 3)
                                    .put("index.number_of_replicas", 1)
    );

    createIndexRequest.mapping(data, XContentType.JSON);
    try {
      boolean created = openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
      log.info("Index " + createIndexRequest.index() + " created:" + created);
    } catch (Exception e) {
      if (e.getMessage().contains("already exists")) {
        log.info("Index " + createIndexRequest.index() + " already exists");
      } else {
        throw e;
      }
    }
  }


  @SneakyThrows
  @PostConstruct
  public void createIndexFromFile () {

    URL url = Resources.getResource("mappings_settings.json");
    String data = Resources.toString(url, Charsets.UTF_8);
    CreateIndexRequest createIndexRequest = new CreateIndexRequest("dogs");
    createIndexRequest.source(data, XContentType.JSON);
    try {
      boolean created = openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
      log.info("Index " + createIndexRequest.index() + " created:" + created);
    } catch (Exception e) {
      if (e.getMessage().contains("already exists")) {
        log.info("Index " + createIndexRequest.index() + " already exists");
      } else {
        throw e;
      }
    }
  }


}
