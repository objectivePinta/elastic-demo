package com.demo.elasticsearch;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface LogDataRepository extends ElasticsearchRepository<LogData, String> {

}
