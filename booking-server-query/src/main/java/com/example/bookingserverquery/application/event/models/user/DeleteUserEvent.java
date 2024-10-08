package com.example.bookingserverquery.application.event.models.user;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteUserEvent {
    List<String> ids;
}
