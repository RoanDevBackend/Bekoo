package com.example.bookingserverquery.application.handler.doctor;

import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.FindByNameResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.application.service.i.DoctorService;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByDoctorNameHandler {

    final DoctorRepository doctorRepository;
    final DoctorService doctorService;

    public FindByNameResponse<DoctorResponse> findByName(FindByNameQuery<DoctorResponse> query){

        Pageable pageable= query.getPageable();

        Page<Doctor> page= doctorRepository.findByName(query.getName(), pageable);

        List<DoctorResponse> doctorResponses= new ArrayList<>();

        for(Doctor x: page.getContent()){
            DoctorResponse doctorResponse= doctorService.toResponse(x);
            doctorResponses.add(doctorResponse);
        }

        return FindByNameResponse.<DoctorResponse>builder()
                .name(query.getName())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(page.getNumber()+1)
                .orders(query.getOrders())
                .contentResponse(doctorResponses)
                .totalElements(page.getTotalElements())
                .build();
    }
}
