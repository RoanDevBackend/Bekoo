package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@ToString
@Document(indexName = "department")
public class Department {
    @Id
    String id;
    @Field(type = FieldType.Text)
    String name;
    @Field(type = FieldType.Text)
    String description;
}
