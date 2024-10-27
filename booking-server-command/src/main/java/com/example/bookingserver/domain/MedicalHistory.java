package com.example.bookingserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@ToString
public class MedicalHistory extends EntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "varchar(80)")
    String name;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @Column(nullable = false)
    LocalDate dateOfVisit; // Ngày khám

    @Column(columnDefinition = "varchar(255)")
    String diagnosis; // Chẩn đoán

    @Column(columnDefinition = "text")
    String treatment; // Điều trị hoặc ghi chú

    @Column(columnDefinition = "text")
    String doctorNotes; // Ghi chú của bác sĩ

    @Column(columnDefinition = "varchar(255)")
    String prescribedMedication; // Thuốc được kê đơn
}
