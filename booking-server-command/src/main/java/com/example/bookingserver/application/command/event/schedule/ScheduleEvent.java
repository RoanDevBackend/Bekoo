package com.example.bookingserver.application.command.event.schedule;


import com.example.bookingserver.application.command.event.EventBase;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleEvent extends EventBase {
    String id;
    String patientId;
    String doctorId;
    String specializeId;
    LocalDateTime checkIn;
    String note;
    String status;
}
