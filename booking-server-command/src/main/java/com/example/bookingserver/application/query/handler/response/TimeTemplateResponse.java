package com.example.bookingserver.application.query.handler.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTemplateResponse {
    String time;
    boolean isAvailable;
}
