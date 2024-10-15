package com.example.bookingserverquery.application.event.models.doctor_department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorDepartmentEvent {
    String doctorId;
    String departmentId;
}
