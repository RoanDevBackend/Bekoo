package com.example.bookingserverquery.application.event.models.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
