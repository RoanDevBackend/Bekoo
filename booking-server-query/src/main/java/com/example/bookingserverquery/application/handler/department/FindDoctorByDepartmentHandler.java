package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.application.service.i.DoctorService;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.DoctorDepartment;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FindDoctorByDepartmentHandler {
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final DoctorELRepository doctorELRepository;
    final DoctorService doctorService;

    public PageResponse<DoctorResponse> execute(String id, QueryBase<DoctorResponse> queryBase){
        Pageable pageable= PageRequest.of(queryBase.getPageIndex()-1 , queryBase.getPageSize());

        Page<DoctorDepartment> page = doctorDepartmentELRepository.findByDepartmentId(id, pageable);

        List<DoctorResponse> doctorResponses = new ArrayList<>();

        for(DoctorDepartment x: page.getContent()){
            Optional<Doctor> doctorOptional= doctorELRepository.findById(x.getDoctorId());
            if(doctorOptional.isPresent()){
                DoctorResponse doctorResponse= doctorService.toResponse(doctorOptional.get());
                doctorResponses.add(doctorResponse);
            }
        }
        return PageResponse.<DoctorResponse>builder()
                .totalPage(page.getTotalPages())
                .pageIndex(queryBase.getPageIndex())
                .pageSize(queryBase.getPageSize())
                .orders(queryBase.getOrders())
                .contentResponse(doctorResponses)
                .totalElements(page.getTotalElements())
                .build();
    }
}

