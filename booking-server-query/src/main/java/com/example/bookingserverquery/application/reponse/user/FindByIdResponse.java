package com.example.bookingserverquery.application.reponse.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FindByIdResponse {
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
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
