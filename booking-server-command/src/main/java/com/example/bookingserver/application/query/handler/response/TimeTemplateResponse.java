package com.example.bookingserver.application.query.handler.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTemplateResponse {
    String range;
    LocalDateTime timeCheckIn;
    boolean isAvailable;
}
