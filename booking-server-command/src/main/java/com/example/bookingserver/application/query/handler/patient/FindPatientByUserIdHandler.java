package com.example.bookingserver.application.query.handler.patient;


import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindPatientByUserIdHandler {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @SneakyThrows
    @Transactional
    public PatientResponse execute(String patientId) {
        Optional<Patient> patientOptional= patientRepository.findByUserId(patientId);
        if(patientOptional.isEmpty()) return null;
        Patient patient = patientOptional.get();
        return patientMapper.toPatientResponse(patient);
    }
}
