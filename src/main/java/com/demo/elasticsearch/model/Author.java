package com.demo.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Builder
@AllArgsConstructor
public class Author {

    @Field(type = Text, name = "firstName")
    String firstName;

    @Field(type = Text, name = "lastName")
    String lastName;
}
