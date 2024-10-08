package com.example.bookingserver.application.event.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoUserEvent {
    String id;
    String name;
    String cccd;
    String province;
    String district;
    String commune;
    String aboutAddress;
    String dob;
    String gender;
}
