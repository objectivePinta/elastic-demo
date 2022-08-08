package com.demo.elasticsearch.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.common.settings.Settings;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@AllArgsConstructor
@Slf4j
public class IndexManagerService {

    private final RestHighLevelClient openSearchClient;


    @SneakyThrows
    @PostConstruct
    public void createIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("cats");

        createIndexRequest.settings(Settings.builder() //Specify in the settings how many shards you want in the index.
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 1)
        );

        HashMap<String, String> integerMapping = new HashMap<String,String>();
        integerMapping.put("type", "integer");

        HashMap<String, Object> ageMapping = new HashMap<String, Object>();
        ageMapping.put("age", integerMapping);

        HashMap<String, String> textMapping = new HashMap<String,String>();
        textMapping.put("type", "text");

        HashMap<String, Object> nameMapping = new HashMap<String, Object>();
        ageMapping.put("name", textMapping);

        HashMap<String, Object> mapping = new HashMap<String, Object>();
        mapping.put("properties", ageMapping);
        mapping.put("properties", nameMapping);


        createIndexRequest.mapping(mapping);
        boolean created = openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
        log.info("Index created:"+created);
    }



}
