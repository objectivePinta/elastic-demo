package com.demo.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Builder
@AllArgsConstructor
public class Author {

    @Field(type = Text, name = "firstName")
    String firstName;

    @Field(type = Text, name = "lastName")
    String lastName;

    @Field(type = FieldType.Float, name = "salary")
    BigDecimal salary;

    @Field(type = FieldType.Date, name = "date_of_employment")
    LocalDate dateOfEmployment;

}
