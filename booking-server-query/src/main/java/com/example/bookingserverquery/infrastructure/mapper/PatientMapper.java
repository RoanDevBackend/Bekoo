package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.domain.EmergencyContact;
import com.example.bookingserverquery.domain.MedicalHistory;
import com.example.bookingserverquery.domain.Patient;
import document.event.patient.EmergencyContactEvent;
import document.event.patient.MedicalHistoryEvent;
import document.event.patient.PatientEvent;
import document.response.EmergencyContactResponse;
import document.response.MedicalHistoryResponse;
import document.response.PatientResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toPatient(PatientEvent event);
    PatientResponse toPatientResponse(Patient patient);

    MedicalHistory toMedicalHistory(MedicalHistoryEvent event);
    MedicalHistoryResponse toMedicalHistoryResponse(MedicalHistory history);

    EmergencyContact toEmergencyContact(EmergencyContactEvent event);
    EmergencyContactResponse toEmergencyContactResponse(EmergencyContact emergencyContact);
}
