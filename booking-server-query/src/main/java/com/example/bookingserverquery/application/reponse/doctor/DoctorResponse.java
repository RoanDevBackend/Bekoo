package com.example.bookingserverquery.application.reponse.doctor;

import com.example.bookingserverquery.application.reponse.user.FindByUserIdResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    String id;
    String description;
    String trainingBy;
    FindByUserIdResponse user;
}
