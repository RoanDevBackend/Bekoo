package com.example.bookingserverquery.application.handler.patient;


import com.example.bookingserverquery.domain.EmergencyContact;
import com.example.bookingserverquery.domain.Patient;
import com.example.bookingserverquery.infrastructure.mapper.PatientMapper;
import com.example.bookingserverquery.infrastructure.repository.EmergencyContactELRepository;
import com.example.bookingserverquery.infrastructure.repository.PatientELRepository;
import document.response.PatientResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindPatientByUserIdHandler {
    private final PatientELRepository patientRepository;
    private final PatientMapper patientMapper;
    private final EmergencyContactELRepository emergencyContactELRepository;

    @SneakyThrows
    @Transactional
    public PatientResponse execute(String userId) {
        Optional<Patient> patientOptional= patientRepository.findByUserId(userId);
        if(patientOptional.isEmpty()) return null;


        Patient patient = patientOptional.get();
        Page<EmergencyContact> page= emergencyContactELRepository.findAllByPatientId(patient.getId(), Pageable.unpaged());
        PatientResponse response= patientMapper.toPatientResponse(patient);
        response.setEmergencyContacts(page.stream().map(patientMapper::toEmergencyContactResponse).toList());
        return response;
    }
}
