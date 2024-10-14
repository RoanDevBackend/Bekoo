package com.example.bookingserver.domain.idClass;

import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDepartmentId implements Serializable {
    Doctor doctor;
    Department department;
}
