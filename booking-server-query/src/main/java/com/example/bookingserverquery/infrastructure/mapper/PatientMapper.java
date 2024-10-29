package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.event.models.patient.EmergencyContactEvent;
import com.example.bookingserverquery.application.event.models.patient.MedicalHistoryEvent;
import com.example.bookingserverquery.application.event.models.patient.PatientEvent;
import com.example.bookingserverquery.domain.EmergencyContact;
import com.example.bookingserverquery.domain.MedicalHistory;
import com.example.bookingserverquery.domain.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toPatient(PatientEvent event);

    MedicalHistory toMedicalHistory(MedicalHistoryEvent event);

    EmergencyContact toEmergencyContact(EmergencyContactEvent event);
}
