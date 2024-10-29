package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.domain.MedicalHistory;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import com.example.bookingserver.infrastructure.persistence.repository.MedicalHistoryRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletePatientHandler {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final MessageProducer messageProducer;

    public void deleteMedicalHistory(Long medicalHistoryId) {
        medicalHistoryRepository.deleteById(medicalHistoryId);
        messageProducer.sendMessage("delete-medical-history", ApplicationConstant.EventType.DELETE, medicalHistoryId+"", medicalHistoryId+"", "Medical History");
    }

    public void deleteEmergencyContact(Long emergencyContactId) {
        Optional<EmergencyContact> emergencyContact= emergencyContactRepository.findById(emergencyContactId);
        if(emergencyContact.isPresent()) {
            if (emergencyContactRepository.countPatient(emergencyContact.get().getPatient().getId()) > 1) {
                emergencyContactRepository.deleteById(emergencyContactId);
                messageProducer.sendMessage("delete-emergency-contact", ApplicationConstant.EventType.DELETE, emergencyContactId+"", emergencyContactId+"", "Emergency Contact");
            }
            else{
                throw new RuntimeException("Bạn cần giữ lại một liên hệ khẩn cấp");
            }
        }else{
            throw new RuntimeException("Không tồn tại");
        }
    }
}
