package com.demo.elasticsearch;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchClientConfiguration extends AbstractElasticsearchConfiguration
{

	@Override
	@Bean
	public org.elasticsearch.client.RestHighLevelClient elasticsearchClient ()
	{
		final ClientConfiguration clientConfiguration =
				ClientConfiguration.builder().connectedTo("localhost:9200").build();

		return RestClients.create(clientConfiguration).rest();
	}

	@Bean
	public ElasticsearchOperations elasticsearchOperations(RestHighLevelClient elasticsearchClient) {
		return new ElasticsearchRestTemplate(elasticsearchClient);
	}

//  @Bean
//  public ElasticsearchClient elasticSearchClient ()
//  {
//    // Create the low-level client
//    RestClient httpClient = RestClient.builder(
//        new HttpHost("localhost", 9200)
//    ).build();
//
//// Create the Java API Client with the same low level client
//    ElasticsearchTransport transport = new RestClientTransport(
//        httpClient,
//        new JacksonJsonpMapper()
//    );
//
//    return new ElasticsearchClient(transport);
//
//// hlrc and esClient share the same httpClient
//  }httpClient

  @Bean
  public ElasticsearchRestTemplate elasticsearchRestTemplate(org.elasticsearch.client.RestHighLevelClient elasticsearchClient)
  {
    return new ElasticsearchRestTemplate(elasticsearchClient);
  }
}
