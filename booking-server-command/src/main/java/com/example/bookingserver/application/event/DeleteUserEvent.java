package com.example.bookingserver.application.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteUserEvent {
    List<String> ids;
}
