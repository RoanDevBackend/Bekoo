package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.application.command.command.patient.CreateInfoPatientCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import document.constant.TopicConstant;
import document.event.patient.EmergencyContactEvent;
import document.event.patient.PatientEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateInfoPatientHandler {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientMapper patientMapper;
    private final MessageProducer messageProducer;
    final String TOPIC_1= TopicConstant.PatientTopic.CREATE_PATIENT;

    @Transactional
    @SneakyThrows
    public PatientResponse execute(CreateInfoPatientCommand command) {
        if(patientRepository.findByUserId(command.getUserId()).isPresent()) {
            throw new RuntimeException("Thông tin bệnh nhân đã tồn tại cho người dùng này");
        }
        User user = userRepository.findById(command.getUserId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );

        Patient patient= patientMapper.toPatient(command);
        patient.setUser(user);

        EmergencyContact emergencyContact =
                patientMapper.toEmergencyContact(command.getEmergencyContactCommand());
        List<EmergencyContact> emergencyContacts = new ArrayList<>();

        emergencyContacts.add(emergencyContact);
        patient.setEmergencyContacts(emergencyContacts);

        patient= patientRepository.save(patient);

        emergencyContact.setPatient(patient);

        emergencyContactRepository.save(emergencyContact);

        PatientEvent patientEvent= patientMapper.toPatientEvent(patient);
        EmergencyContactEvent emergencyContactEvent= patientMapper.toEmergencyContactEvent(emergencyContact);
        patientEvent.setEmergencyContact(emergencyContactEvent);

        messageProducer.sendMessage(TOPIC_1, "ADD", patientEvent, patient.getId(), "Patient");

        return patientMapper.toPatientResponse(patient);
    }
}
