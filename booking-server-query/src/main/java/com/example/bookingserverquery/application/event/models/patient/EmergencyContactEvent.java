package com.example.bookingserverquery.application.event.models.patient;

import com.example.bookingserverquery.application.event.models.EventBase;
import com.example.bookingserverquery.domain.Patient;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmergencyContactEvent extends EventBase {
    Long id;
    String name;
    String phone;
    String address;
    String relationship;
    String patientId;
}
