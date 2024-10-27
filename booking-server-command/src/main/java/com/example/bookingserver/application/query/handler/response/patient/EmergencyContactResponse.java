package com.example.bookingserver.application.query.handler.response.patient;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmergencyContactResponse {
    String id;
    String name;
    String phone;
    String address;
    String relationship;
}
