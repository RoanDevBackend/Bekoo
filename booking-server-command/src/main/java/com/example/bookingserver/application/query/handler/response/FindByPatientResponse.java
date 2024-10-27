package com.example.bookingserver.application.query.handler.response;

import com.example.bookingserver.application.command.reponse.DoctorResponse;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindByPatientResponse {
    String id;
    DoctorResponse doctor;
    SpecializeResponse specialize;
    @JsonProperty(value = "status")
    String active;
    LocalDateTime checkIn;
}
