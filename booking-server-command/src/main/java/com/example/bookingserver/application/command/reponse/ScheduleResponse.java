package com.example.bookingserver.application.command.reponse;


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
    UserResponse user;
    DoctorResponse doctor;
    SpecializeResponse specialize;
    LocalDateTime checkIn;
    String active;
}
