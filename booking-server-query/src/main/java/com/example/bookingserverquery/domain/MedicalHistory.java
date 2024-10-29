package com.example.bookingserverquery.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Document(indexName = "medical_history")
public class MedicalHistory extends EntityBase{
    @Id
    Long id;
    String name;
    @Field(type = FieldType.Nested)
    Patient patient;
    @Field(type = FieldType.Date)
    LocalDate dateOfVisit;
    @Field(type = FieldType.Text)
    String diagnosis;
    @Field(type = FieldType.Text)
    String treatment;
    @Field(type = FieldType.Text)
    String doctorNotes;
    @Field(type = FieldType.Text)
    String prescribedMedication; // Thuốc được kê đơn
}
