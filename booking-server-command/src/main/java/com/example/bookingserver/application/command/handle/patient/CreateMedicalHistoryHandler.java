package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.application.command.command.patient.CreateMedicalHistoryCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.MedicalHistory;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.MedicalHistoryRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import document.constant.TopicConstant;
import document.event.patient.MedicalHistoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CreateMedicalHistoryHandler {
    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientMapper patientMapper;
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.PatientTopic.CREATE_MEDICAL;

    @SneakyThrows
    public void execute(CreateMedicalHistoryCommand command) {
        Patient patient = patientRepository.findById(command.getPatientId()).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        LocalDate date= LocalDate.parse(command.getDateOfVisit());
        if(date.isAfter(LocalDate.now())){
            throw new RuntimeException("Ngày không hợp lệ");
        }
        MedicalHistory medicalHistory= patientMapper.toMedicalHistory(command);
        medicalHistory.setPatient(patient);

        medicalHistoryRepository.save(medicalHistory);

        MedicalHistoryEvent medicalHistoryEvent= patientMapper.toMedicalHistoryEvent(medicalHistory);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.ADD, medicalHistoryEvent, medicalHistory.getId()+"", "Medical History");
    }
}
