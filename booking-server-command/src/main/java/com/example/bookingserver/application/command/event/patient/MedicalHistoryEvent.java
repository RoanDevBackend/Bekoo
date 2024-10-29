package com.example.bookingserver.application.command.event.patient;

import com.example.bookingserver.application.command.event.EventBase;
import com.example.bookingserver.domain.Patient;
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
