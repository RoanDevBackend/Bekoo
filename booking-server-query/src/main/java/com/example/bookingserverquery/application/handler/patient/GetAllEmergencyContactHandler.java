package com.example.bookingserverquery.application.handler.patient;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.domain.EmergencyContact;
import com.example.bookingserverquery.infrastructure.mapper.PatientMapper;
import com.example.bookingserverquery.infrastructure.repository.EmergencyContactELRepository;
import document.response.EmergencyContactResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllEmergencyContactHandler {
    private final EmergencyContactELRepository emergencyContactRepository;
    private final PatientMapper patientMapper;

    public PageResponse<EmergencyContactResponse> execute(String patientId, QueryBase<EmergencyContactResponse> queryBase) {
        Page<EmergencyContact> page= emergencyContactRepository.findAllByPatientId(patientId, queryBase.getPageable());
        List<EmergencyContactResponse> contents= new ArrayList<>();
        for(EmergencyContact contact : page.getContent()){
            contents.add(patientMapper.toEmergencyContactResponse(contact));
        }
        return PageResponse.<EmergencyContactResponse>builder()
                .pageIndex(queryBase.getPageIndex())
                .pageSize(queryBase.getPageSize())
                .totalElements(page.getTotalElements())
                .contentResponse(contents)
                .totalPage(page.getTotalPages())
                .build();
    }
}
