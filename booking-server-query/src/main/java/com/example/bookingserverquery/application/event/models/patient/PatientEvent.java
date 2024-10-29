package com.example.bookingserverquery.application.event.models.patient;

import com.example.bookingserverquery.application.event.models.user.CreateUserEvent;
import com.example.bookingserverquery.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientEvent {
    String id;
    String healthInsuranceNumber;
    String bloodType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userId;
}
