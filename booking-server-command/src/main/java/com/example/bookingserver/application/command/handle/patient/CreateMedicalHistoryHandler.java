package com.example.bookingserver.application.command.handle.patient;


import com.example.bookingserver.application.command.command.patient.CreateMedicalHistoryCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.MedicalHistory;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.persistence.repository.MedicalHistoryRepository;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateMedicalHistoryHandler {
    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientMapper patientMapper;

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
    }
}
