package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class Schedule extends EntityBase{
    @Id
    String id;
    @Field(type = FieldType.Nested)
    User user;
    @Field(type = FieldType.Nested)
    Specialize specialize;
    @Field(type = FieldType.Nested)
    Doctor doctor;
    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS", format = {})
    LocalDateTime checkIn;
    @Field(type = FieldType.Text)
    String note;
    @Field(type = FieldType.Boolean)
    boolean active=true;
}
