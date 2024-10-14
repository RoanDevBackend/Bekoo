package com.example.bookingserver.application.command.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDoctorCommand {
    String trainingBy;
    String description;
    String user_id;
}
