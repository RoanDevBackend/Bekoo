package com.example.bookingserver.application.command.event.user;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
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
    Set<String> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
