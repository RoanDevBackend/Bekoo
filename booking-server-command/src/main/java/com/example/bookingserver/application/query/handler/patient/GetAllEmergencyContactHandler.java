package com.example.bookingserver.application.query.handler.patient;

import com.example.bookingserver.application.query.handler.response.patient.EmergencyContactResponse;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.domain.EmergencyContact;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.persistence.repository.EmergencyContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllEmergencyContactHandler {
    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientMapper patientMapper;

    public PageResponse<EmergencyContactResponse> execute(String patientId, Pageable pageable) {
        Page<EmergencyContact> page= emergencyContactRepository.findAllByPatientId(patientId, pageable);
        List<EmergencyContactResponse> contents= new ArrayList<>();
        for(EmergencyContact contact : page.getContent()){
            contents.add(patientMapper.toEmergencyContactResponse(contact));
        }
        return PageResponse.<EmergencyContactResponse>builder()
                .pageIndex(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .contentResponse(contents)
                .totalPage(page.getTotalPages())
                .build();
    }
}
