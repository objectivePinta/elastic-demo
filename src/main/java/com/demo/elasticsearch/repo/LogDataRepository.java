package com.demo.elasticsearch.repo;


import com.demo.elasticsearch.model.LogData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface LogDataRepository extends ElasticsearchRepository<LogData, String> {

}
