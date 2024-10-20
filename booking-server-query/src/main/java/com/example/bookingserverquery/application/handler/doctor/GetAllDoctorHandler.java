package com.example.bookingserverquery.application.handler.doctor;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.repository.DoctorRepository;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GetAllDoctorHandler {

    final DoctorRepository doctorRepository;
    final DoctorMapper doctorMapper;

    public PageResponse<DoctorResponse> getAll(QueryBase<DoctorResponse> query){
        Page<Doctor> page= doctorRepository.getAll(query.getPageable());
        List<DoctorResponse> doctorResponses= new ArrayList<>();
        for(Doctor x: page.getContent()){
            DoctorResponse doctorResponse= doctorMapper.toResponse(x);
            doctorResponses.add(doctorResponse);
        }
        return PageResponse.<DoctorResponse>builder()
                .contentResponse(doctorResponses)
                .pageIndex(page.getNumber() + 1)
                .pageSize(page.getSize())
                .orders(query.getOrders())
                .totalPage(page.getTotalPages())
                .build();
    }

}
