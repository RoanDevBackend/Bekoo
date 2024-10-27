package com.example.bookingserver.domain;

import com.example.bookingserver.domain.idClass.DoctorDepartmentId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@IdClass(DoctorDepartmentId.class)
public class DoctorDepartment {

    @Id
            @ManyToOne
            @JoinColumn(name = "doctor_id")
    Doctor doctor;
    @Id
            @ManyToOne
            @JoinColumn(name = "department_id")
    Department department;
}
