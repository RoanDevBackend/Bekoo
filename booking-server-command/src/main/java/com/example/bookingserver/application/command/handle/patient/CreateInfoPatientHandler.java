package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.application.command.command.patient.CreateInfoPatientCommand;
import com.example.bookingserver.application.command.event.patient.EmergencyContactEvent;
import com.example.bookingserver.application.command.event.patient.PatientEvent;
import com.example.bookingserver.application.command.event.user.CreateUserEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateInfoPatientHandler {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientMapper patientMapper;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final String TOPIC_1="create-info-patient";
    private final String TOPIC_2="create-info-contact";

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

        patient= patientRepository.save(patient);

        emergencyContact.setPatient(patient);

        emergencyContactRepository.save(emergencyContact);

        PatientEvent patientEvent= patientMapper.toPatientEvent(patient);


        EmergencyContactEvent emergencyContactEvent= patientMapper.toEmergencyContactEvent(emergencyContact);

        ;

        messageProducer.sendMessage(TOPIC_1, "ADD", patientEvent, patient.getId(), "Patient");
        messageProducer.sendMessage(TOPIC_2, "ADD", emergencyContactEvent, emergencyContact.getId()+"", "Emergency Contact");

        return patientMapper.toPatientResponse(patient);
    }
}
