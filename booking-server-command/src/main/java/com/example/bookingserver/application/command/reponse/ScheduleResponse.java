package com.example.bookingserver.application.command.reponse;


import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponse {
    String id;
    PatientResponse patient;
    DoctorResponse doctor;
    SpecializeResponse specialize;
    LocalDateTime checkIn;
    String status;
}
