package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "doctor_department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorDepartment {
    @Id
    String id;
    @Field(type = FieldType.Keyword)
    String doctorId;
    @Field(type = FieldType.Keyword)
    String departmentId;
}
