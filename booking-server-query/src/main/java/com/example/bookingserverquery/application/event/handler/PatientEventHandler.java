package com.example.bookingserverquery.application.event.handler;

import com.example.bookingserverquery.infrastructure.repository.EmergencyContactELRepository;
import com.example.bookingserverquery.infrastructure.repository.MedicalHistoryELRepository;
import com.example.bookingserverquery.infrastructure.repository.PatientELRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientEventHandler {
    PatientELRepository patientRepository;
    EmergencyContactELRepository emergencyContactRepository;
    MedicalHistoryELRepository medicalHistoryRepository;
}
