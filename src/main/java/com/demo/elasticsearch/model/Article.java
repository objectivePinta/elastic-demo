package com.demo.elasticsearch.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Routing;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Document(indexName = "blog", createIndex = true)
@TypeAlias("blog")
@Data
@Builder
@Routing("topic")
public class Article {
    @Id
    private long id;

    @Field(type = Text)
    private String title;

    @Field(type = Keyword)
    @NotNull
    private Status status;

    @Field(type = Date)
    private LocalDateTime lastEdited;


    //    @Field(type = Keyword)
    private List<String> tags;

    @Field(type = FieldType.Integer)
    private int numberOfWords;

    @Field(type = Text)
    private String topic;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Author> authors;
}
