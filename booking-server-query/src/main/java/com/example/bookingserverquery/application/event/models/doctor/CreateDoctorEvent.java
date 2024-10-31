package com.example.bookingserverquery.application.event.models.doctor;

import com.example.bookingserverquery.application.event.models.EventBase;
import com.example.bookingserverquery.application.event.models.user.CreateUserEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDoctorEvent extends EventBase {
    String id;
    String trainingBy;
    String description;
    CreateUserEvent user;
}
