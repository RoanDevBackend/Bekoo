package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import com.example.bookingserver.infrastructure.persistence.repository.MedicalHistoryRepository;
import document.constant.TopicConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeletePatientHandler {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final MessageProducer messageProducer;
    final String TOPIC_1= TopicConstant.PatientTopic.DELETE_MEDICAL;
    final String TOPIC_2= TopicConstant.PatientTopic.CREATE_CONTACT;

    public void deleteMedicalHistory(Long medicalHistoryId) {
        medicalHistoryRepository.deleteById(medicalHistoryId);
        messageProducer.sendMessage(TOPIC_1, ApplicationConstant.EventType.DELETE, medicalHistoryId+"", medicalHistoryId+"", "Medical History");
    }

    public void deleteEmergencyContact(Long emergencyContactId) {
        Optional<EmergencyContact> emergencyContact= emergencyContactRepository.findById(emergencyContactId);
        if(emergencyContact.isPresent()) {
            if (emergencyContactRepository.countPatient(emergencyContact.get().getPatient().getId()) > 1) {
                emergencyContactRepository.deleteById(emergencyContactId);
                messageProducer.sendMessage(TOPIC_2, ApplicationConstant.EventType.DELETE, emergencyContactId+"", emergencyContactId+"", "Emergency Contact");
            }
            else{
                throw new RuntimeException("Bạn cần giữ lại một liên hệ khẩn cấp");
            }
        }else{
            throw new RuntimeException("Không tồn tại");
        }
    }
}
