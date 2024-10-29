package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class Patient extends EntityBase{
    @Id
            @Field(type = FieldType.Keyword)
    String id;
    @Field(type = FieldType.Keyword)
    String healthInsuranceNumber;
    @Builder.Default
            @Field(type = FieldType.Keyword)
    String bloodType= "N/A"; // Nhóm máu
    @Field(type = FieldType.Nested)
    User user;
}
