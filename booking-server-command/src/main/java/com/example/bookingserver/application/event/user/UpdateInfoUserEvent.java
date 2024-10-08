package com.example.bookingserver.application.event.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
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
