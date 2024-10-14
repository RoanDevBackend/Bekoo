package com.example.bookingserverquery.application.reponse.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
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
    String description;
    String trainingBy;
}
