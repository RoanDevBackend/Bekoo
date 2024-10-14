package com.example.bookingserver.application.event.doctor;

import com.example.bookingserver.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDoctorEvent {
    String id;
    String trainingBy;
    String description;
    String user_id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
