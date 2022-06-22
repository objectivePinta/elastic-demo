package com.demo.elasticsearch.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "blog")
@Data
@Builder
public class Article {
    @Id
    private String id;

    @Field(type = Text)
    private String title;

    @Field(type = Text)
    private String topic;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Author> authors;
}
