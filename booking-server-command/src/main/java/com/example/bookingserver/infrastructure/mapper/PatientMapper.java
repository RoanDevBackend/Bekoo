package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.command.patient.CreateInfoPatientCommand;
import com.example.bookingserver.application.command.command.patient.CreateMedicalHistoryCommand;
import com.example.bookingserver.application.command.command.patient.EmergencyContactCommand;
import com.example.bookingserver.application.query.handler.response.patient.EmergencyContactResponse;
import com.example.bookingserver.application.query.handler.response.patient.MedicalHistoryResponse;
import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.domain.MedicalHistory;
import com.example.bookingserver.domain.Patient;
import document.event.patient.EmergencyContactEvent;
import document.event.patient.MedicalHistoryEvent;
import document.event.patient.PatientEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toPatient(CreateInfoPatientCommand command);

    @Mapping(target = "userId", source = "user.id")
    PatientEvent toPatientEvent(Patient patient);

    PatientResponse toPatientResponse(Patient patient);

    @Mapping(source = "dateOfVisit", target = "dateOfVisit", qualifiedByName = "convertLocalDate")
    MedicalHistory toMedicalHistory(CreateMedicalHistoryCommand command);

    MedicalHistoryResponse toMedicalHistoryResponse(MedicalHistory command);

    @Mapping(target = "patientId", source = "patient.id")
    MedicalHistoryEvent toMedicalHistoryEvent(MedicalHistory command);

    EmergencyContact toEmergencyContact(EmergencyContactCommand command);

    EmergencyContactResponse toEmergencyContactResponse(EmergencyContact command);
    @Mapping(target = "patientId", source = "patient.id")
    EmergencyContactEvent toEmergencyContactEvent(EmergencyContact command);



    @Named(value = "convertLocalDate")
    default LocalDate convertLocalDate(String date){
        try{
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Định dạng ngày tháng năm không đúng");
        }
    }
}
