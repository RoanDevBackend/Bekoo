package com.example.bookingserverquery.application.reponse.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class FindByUserIdResponse {
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
    Set<String> roles;
    String linkAvatar;
}
