package com.example.bookingserver.application.query.handler.patient;


import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.query.handler.response.patient.PatientResponse;
import com.example.bookingserver.domain.Patient;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindPatientByUserIdHandler {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    @SneakyThrows
    public PatientResponse execute(String patientId) {
        Patient patient= patientRepository.findByUserId(patientId).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        return patientMapper.toPatientResponse(patient);
    }
}
