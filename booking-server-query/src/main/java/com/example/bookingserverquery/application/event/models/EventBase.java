package com.example.bookingserverquery.application.event.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventBase {
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
