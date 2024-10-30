package com.example.bookingserver.application.query.handler.response.patient;

import com.example.bookingserver.application.command.reponse.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse {
    String id;
    String healthInsuranceNumber;
    String bloodType;
    @JsonProperty(value = "info")
    UserResponse user;
    List<EmergencyContactResponse> emergencyContacts;
}
