package com.demo.elasticsearch;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Document(indexName = "logdataindex")
@Data
@Builder
@ToString
public class LogData {

  @Id
  private String id;

  @Field(type = FieldType.Text, name = "host")
  private String host;

  @Field(type = FieldType.Date, name = "date")
  private Date date;

  @Field(type = FieldType.Text, name = "message")
  private String message;

  @Field(type = FieldType.Double, name = "size")
  private double size;

  @Field(type = FieldType.Text, name = "status")
  private String status;

  // Getters and Setters

}
