package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.DoctorDepartment;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindDoctorByDepartmentHandler {
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final DoctorELRepository doctorELRepository;
    final UserELRepository userELRepository;
    final DoctorMapper doctorMapper;
    public PageResponse<DoctorResponse> execute(String id, QueryBase<DoctorResponse> queryBase){
        Page<DoctorDepartment> page= doctorDepartmentELRepository.findByDepartmentId(id, queryBase.getPageable());
        List<DoctorResponse> doctorResponses= new ArrayList<>();
        for(DoctorDepartment x: page.getContent()){
            Optional<Doctor> doctorOptional= doctorELRepository.findById(x.getDoctorId());
            doctorOptional.ifPresent(doctor -> doctorResponses.add(doctorMapper.toResponse(doctor, doctor.getUser())));
        }
        return PageResponse.<DoctorResponse>builder()
                .totalPage(page.getTotalPages())
                .pageIndex(queryBase.getPageIndex())
                .pageSize(queryBase.getPageSize())
                .orders(queryBase.getOrders())
                .contentResponse(doctorResponses)
                .build();
    }
}
