package com.example.bookingserver.application.reponse;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;

    String name;

    String phoneNumber;

    String email;

    String cccd;

    String province;

    String district;

    String commune ;

    String aboutAddress;
    LocalDate dob;
    String gender;
    String linkAvatar;
}
