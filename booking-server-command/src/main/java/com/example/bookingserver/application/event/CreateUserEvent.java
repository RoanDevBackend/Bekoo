package com.example.bookingserver.application.event;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserEvent {
    String id;
    String name;
    String phoneNumber;
    String email;
    String cccd;
    String province;
    String district;
    String commune;
    String aboutAddress;
    String password;
    LocalDate dob;
    String gender;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
