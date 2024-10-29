package com.example.bookingserverquery.application.event.handler;

import com.example.bookingserverquery.application.event.models.patient.EmergencyContactEvent;
import com.example.bookingserverquery.application.event.models.patient.MedicalHistoryEvent;
import com.example.bookingserverquery.application.event.models.patient.PatientEvent;
import com.example.bookingserverquery.domain.EmergencyContact;
import com.example.bookingserverquery.domain.MedicalHistory;
import com.example.bookingserverquery.domain.Patient;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.mapper.PatientMapper;
import com.example.bookingserverquery.infrastructure.repository.EmergencyContactELRepository;
import com.example.bookingserverquery.infrastructure.repository.MedicalHistoryELRepository;
import com.example.bookingserverquery.infrastructure.repository.PatientELRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientEventHandler {
    PatientELRepository patientRepository;
    EmergencyContactELRepository emergencyContactRepository;
    UserELRepository userRepository;
    MedicalHistoryELRepository medicalHistoryRepository;
    PatientMapper patientMapper;
    ObjectMapper objectMapper;


    @KafkaListener(topics = "create-info-patient")
    @SneakyThrows
    public void CreateInfoPatient(String content){
        System.out.println(content);
        PatientEvent event= objectMapper.readValue(content, PatientEvent.class);
        Patient patient= patientMapper.toPatient(event);
        User user= userRepository.findById(event.getUserId()).orElse(null);
        patient.setUser(user);
        patientRepository.save(patient);
        log.info("Create Info Patient, Value: {}", content);
    }

    @KafkaListener(topics = "create-info-contact")
    @SneakyThrows
    public void CreateInfoContact(String content){
        EmergencyContactEvent event= objectMapper.readValue(content, EmergencyContactEvent.class);
        EmergencyContact emergencyContact= patientMapper.toEmergencyContact(event);
        Patient patient= patientRepository.findById(event.getPatientId()).orElse(null);
        emergencyContact.setPatient(patient);
        emergencyContactRepository.save(emergencyContact);
        log.info("Create Info Emergency Contact, Value: {}", content);
    }

    @KafkaListener(topics = "create-medical-history")
    @SneakyThrows
    public void CreateMedicalHistory(String content){
        MedicalHistoryEvent event= objectMapper.readValue(content, MedicalHistoryEvent.class);
        MedicalHistory medicalHistory= patientMapper.toMedicalHistory(event);
        medicalHistory.setPatient(patientRepository.findById(event.getPatientId()).orElse(null));
        medicalHistoryRepository.save(medicalHistory);
        log.info("Create Medical History, Value: {}", content);
    }

    @KafkaListener(topics = "delete-emergency-contact")
    @SneakyThrows
    public void deleteEmergencyContact(String content){
        Long id= objectMapper.readValue(content, Long.class);
        emergencyContactRepository.deleteById(id);
        log.info("Delete Emergency Contact, Value: {}", content);
    }

    @KafkaListener(topics = "delete-medical-history")
    @SneakyThrows
    public void deleteMedicalHistory(String content){
        Long id= objectMapper.readValue(content, Long.class);
        medicalHistoryRepository.deleteById(id);
        log.info("Delete Medical History, Value: {}", content);
    }
}
