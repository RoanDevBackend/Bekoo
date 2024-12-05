package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Document(indexName = "doctor")
public class Doctor extends EntityBase{
    @Id
    @Field(type = FieldType.Keyword)
    String id;

    @Field(type = FieldType.Text)
    String trainingBy;

    @Field(type = FieldType.Text)
    String description;

    @Field(type = FieldType.Integer)
    Integer price;

    @Field(type = FieldType.Integer)
            @Builder.Default
    Integer maximumPeoplePerDay= 0;

    @Field(type = FieldType.Nested)
    User user;
}
