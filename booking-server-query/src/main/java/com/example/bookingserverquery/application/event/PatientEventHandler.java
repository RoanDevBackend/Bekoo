package com.example.bookingserverquery.application.event;

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
import document.constant.TopicConstant;
import document.event.patient.EmergencyContactEvent;
import document.event.patient.MedicalHistoryEvent;
import document.event.patient.PatientEvent;
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

    @KafkaListener(topics = TopicConstant.PatientTopic.CREATE_PATIENT)
    @SneakyThrows
    public void CreateInfoPatient(String content){
        System.out.println(content);
        PatientEvent event= objectMapper.readValue(content, PatientEvent.class);
        Patient patient= patientMapper.toPatient(event);
        User user= userRepository.findById(event.getUserId()).orElse(null);
        patient.setUser(user);
        patientRepository.save(patient);

        EmergencyContactEvent emergencyContactEvent= event.getEmergencyContact();
        EmergencyContact emergencyContact= patientMapper.toEmergencyContact(emergencyContactEvent);
        emergencyContact.setPatient(patient);
        emergencyContactRepository.save(emergencyContact);

        log.info("Create Info Patient, Value: {}", content);
    }

    @KafkaListener(topics = TopicConstant.PatientTopic.CREATE_CONTACT)
    @SneakyThrows
    public void CreateInfoContact(String content){
        EmergencyContactEvent event= objectMapper.readValue(content, EmergencyContactEvent.class);
        EmergencyContact emergencyContact= patientMapper.toEmergencyContact(event);
        Patient patient= patientRepository.findById(event.getPatientId()).orElse(null);
        emergencyContact.setPatient(patient);
        emergencyContactRepository.save(emergencyContact);
        log.info("Create Info Emergency Contact, Value: {}", content);
    }

    @KafkaListener(topics = TopicConstant.PatientTopic.CREATE_MEDICAL)
    @SneakyThrows
    public void CreateMedicalHistory(String content){
        MedicalHistoryEvent event= objectMapper.readValue(content, MedicalHistoryEvent.class);
        MedicalHistory medicalHistory= patientMapper.toMedicalHistory(event);
        medicalHistory.setPatient(patientRepository.findById(event.getPatientId()).orElse(null));
        medicalHistoryRepository.save(medicalHistory);
        log.info("Create Medical History, Value: {}", content);
    }

    @KafkaListener(topics = TopicConstant.PatientTopic.DELETE_CONTACT)
    @SneakyThrows
    public void deleteEmergencyContact(String content){
        Long id= objectMapper.readValue(content, Long.class);
        emergencyContactRepository.deleteById(id);
        log.info("Delete Emergency Contact, Value: {}", content);
    }

    @KafkaListener(topics = TopicConstant.PatientTopic.DELETE_MEDICAL)
    @SneakyThrows
    public void deleteMedicalHistory(String content){
        Long id= objectMapper.readValue(content, Long.class);
        medicalHistoryRepository.deleteById(id);
        log.info("Delete Medical History, Value: {}", content);
    }

}
