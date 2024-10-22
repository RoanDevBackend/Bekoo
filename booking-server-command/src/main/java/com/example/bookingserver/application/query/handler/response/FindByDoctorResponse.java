package com.example.bookingserver.application.query.handler.response;

import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.application.command.reponse.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindByDoctorResponse {
    String id;
    UserResponse user;
    SpecializeResponse specialize;
    String active;
    LocalDateTime checkIn;
}
