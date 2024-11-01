package com.example.bookingserver.application.command.reponse;


import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetInfoByTokenResponse {
    @Builder.Default
    boolean isDoctor= true;
    DoctorResponse doctor;
    PatientResponse patient;
}
