package com.example.bookingserverquery.application.handler.patient;


import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.domain.MedicalHistory;
import com.example.bookingserverquery.infrastructure.mapper.PatientMapper;
import com.example.bookingserverquery.infrastructure.repository.MedicalHistoryELRepository;
import document.response.MedicalHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllMedicalHistoryHandler {
    private final MedicalHistoryELRepository medicalHistoryRepository;
    private final PatientMapper patientMapper;

    public PageResponse<MedicalHistoryResponse> execute(String patientId, QueryBase<MedicalHistoryResponse> query){
        Page<MedicalHistory> page= medicalHistoryRepository.findAllByPatientId(patientId, query.getPageable());
        List<MedicalHistoryResponse> contents= new ArrayList<>();
        for(MedicalHistory medicalHistory: page.getContent()){
            contents.add(patientMapper.toMedicalHistoryResponse(medicalHistory));
        }
        return PageResponse.<MedicalHistoryResponse>builder()
                .pageIndex(query.getPageIndex())
                .pageSize(query.getPageSize())
                .totalElements(page.getTotalElements())
                .contentResponse(contents)
                .totalPage(page.getTotalPages())
                .build();
    }
}
