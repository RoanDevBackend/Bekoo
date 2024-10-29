package com.example.bookingserverquery.application.event.models.patient;

import com.example.bookingserverquery.application.event.models.EventBase;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalHistoryEvent extends EventBase {
    Long id;
    String name;
    String patientId;
    LocalDate dateOfVisit;
    String diagnosis;
    String treatment;
    String doctorNotes;
    String prescribedMedication;
}
