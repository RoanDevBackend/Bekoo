package com.example.bookingserver.application.command.event.patient;

import com.example.bookingserver.application.command.event.user.CreateUserEvent;
import com.example.bookingserver.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientEvent{
    String id;
    String healthInsuranceNumber;
    String bloodType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userId;
}
