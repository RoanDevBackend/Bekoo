package com.example.bookingserver.application.command.command.schedule;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitScheduleCommand {

    String id;

    String createAt;

    String updatedAt;

    String checkIn;

    String note;

    int paymentStatus;

    int statusId;

    String doctorId;

    String patientId;

    String specializeId;
}
