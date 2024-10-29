package com.example.bookingserver.application.command.handle.patient;

import com.example.bookingserver.application.command.command.patient.EmergencyContactCommand;
import com.example.bookingserver.application.command.event.patient.EmergencyContactEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewEmergencyContactHandler {
    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final MessageProducer  messageProducer;
    private final String TOPIC= "create-info-contact";

    @SneakyThrows
    public void execute(String patientId, EmergencyContactCommand command){
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        EmergencyContact emergencyContact= patientMapper.toEmergencyContact(command);
        emergencyContact.setPatient(patient);
        emergencyContactRepository.save(emergencyContact);

        EmergencyContactEvent event= patientMapper.toEmergencyContactEvent(emergencyContact);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.ADD, event, event.getId()+"", "");
    }
}
