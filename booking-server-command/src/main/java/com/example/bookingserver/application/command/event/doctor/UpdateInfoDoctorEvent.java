package com.example.bookingserver.application.command.event.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDoctorEvent {
    String id;
    String trainingBy;
    String description;
    LocalDateTime updatedAt;
}
