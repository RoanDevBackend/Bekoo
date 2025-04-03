package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.FindByNameResponse;
import com.example.bookingserverquery.domain.Department;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByDepartmentNameHandler {

    final DepartmentELRepository departmentELRepository;
    final SpecializeELRepository specializeELRepository;
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final DepartmentMapper departmentMapper;

    public FindByNameResponse<DepartmentResponse> execute(FindByNameQuery<DepartmentResponse> query){
        Pageable pageable= PageRequest.of(query.getPageIndex()-1, query.getPageSize());//Không có trường createdAt mặc định

        Page<Department> page = query.getName() == null ? departmentELRepository.findAll(pageable) : departmentELRepository.findDepartmentsByName(query.getName(), pageable);

        List<DepartmentResponse> departmentResponses= new ArrayList<>();

        for(Department x: page.getContent()){
            DepartmentResponse departmentResponse = departmentMapper.toResponse(x);
            Integer totalDoctor= doctorDepartmentELRepository.countByDepartmentId(x.getId());
            Long totalSpecialize = specializeELRepository.countByDepartment(x.getId());
            departmentResponse.setTotalDoctor(totalDoctor);
            departmentResponse.setTotalSpecialize(totalSpecialize);
            departmentResponses.add(departmentResponse);
        }

        return FindByNameResponse.<DepartmentResponse>builder()
                .name(query.getName())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(query.getPageIndex())
                .orders(query.getOrders())
                .contentResponse(departmentResponses)
                .totalElements(page.getTotalElements())
                .build();
    }

}
