package com.example.bookingserverquery.application.reponse.user;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    String name;
    String email;
    String phoneNumber;
    String linkAvatar;
    String gender;
}
