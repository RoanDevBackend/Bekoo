package com.example.bookingserver.application.command.command.patient;


import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateInfoPatientCommand {
    String userId;
    String healthInsuranceNumber;
    String bloodType= "N/A";
    EmergencyContactCommand emergencyContactCommand;
}
