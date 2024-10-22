package com.example.bookingserver.application.query.handler.response;

import com.example.bookingserver.application.command.reponse.DoctorResponse;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindByUserResponse {
    String id;
    DoctorResponse doctor;
    SpecializeResponse specialize;
    String active;
    LocalDateTime checkIn;
}
