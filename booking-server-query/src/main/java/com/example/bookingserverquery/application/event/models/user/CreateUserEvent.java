package com.example.bookingserverquery.application.event.models.user;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    Set<String> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
