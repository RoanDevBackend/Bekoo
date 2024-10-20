package com.example.bookingserver.application.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse{
    String id;
    String description;
    String trainingBy;
    UserResponse user;
}
