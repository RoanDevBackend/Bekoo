package com.example.bookingserverquery.application.event.models.schedule;


import com.example.bookingserverquery.application.event.models.EventBase;
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
