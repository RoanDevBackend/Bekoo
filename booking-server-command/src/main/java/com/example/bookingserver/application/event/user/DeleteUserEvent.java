package com.example.bookingserver.application.event.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class DeleteUserEvent {
    List<String> ids;
}
