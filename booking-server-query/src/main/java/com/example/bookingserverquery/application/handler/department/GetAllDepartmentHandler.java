package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.department.DepartmentResponse;
import com.example.bookingserverquery.domain.Department;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllDepartmentHandler {
    final DepartmentMapper departmentMapper;
    final DepartmentELRepository departmentELRepository;


    public PageResponse<DepartmentResponse> execute(QueryBase<DepartmentResponse> query){
        Pageable pageable= PageRequest.of(query.getPageIndex()-1, query.getPageSize());
        Page<Department> page= departmentELRepository.findAll(pageable);
        List<DepartmentResponse> departmentResponses= new ArrayList<>();
        for(Department x: page.getContent()){
            departmentResponses.add(departmentMapper.toResponse(x));
        }
        return PageResponse.<DepartmentResponse>builder()
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(page.getNumber() + 1)
                .orders(query.getOrders())
                .contentResponse(departmentResponses)
                .totalElements(page.getTotalElements())
                .build();
    }

}
