package com.example.bookingserver.application.query.handler.patient;


import com.example.bookingserver.application.query.handler.response.patient.MedicalHistoryResponse;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.domain.MedicalHistory;
import com.example.bookingserver.infrastructure.mapper.PatientMapper;
import com.example.bookingserver.infrastructure.persistence.repository.MedicalHistoryRepository;
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
public class GetAllMedicalHistoryHandler {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientMapper patientMapper;

    public PageResponse<MedicalHistoryResponse> execute(String patientId, Pageable pageable){
        Page<MedicalHistory> page= medicalHistoryRepository.findAllByPatientId(patientId, pageable);
        List<MedicalHistoryResponse> contents= new ArrayList<>();
        for(MedicalHistory medicalHistory: page.getContent()){
            contents.add(patientMapper.toMedicalHistoryResponse(medicalHistory));
        }
        return PageResponse.<MedicalHistoryResponse>builder()
                .pageIndex(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .contentResponse(contents)
                .totalPage(page.getTotalPages())
                .build();
    }
}
