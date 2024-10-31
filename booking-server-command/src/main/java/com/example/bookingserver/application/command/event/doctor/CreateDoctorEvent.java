package com.example.bookingserver.application.command.event.doctor;

import com.example.bookingserver.application.command.event.EventBase;
import com.example.bookingserver.application.command.event.user.CreateUserEvent;
import com.example.bookingserver.domain.User;
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
