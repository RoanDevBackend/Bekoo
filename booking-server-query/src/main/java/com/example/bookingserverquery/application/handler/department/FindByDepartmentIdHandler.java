package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.application.reponse.department.DepartmentResponse;
import com.example.bookingserverquery.application.reponse.department.FindByDepartmentIdResponse;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByDepartmentIdHandler {

    final DepartmentELRepository departmentELRepository;
    final DepartmentMapper departmentMapper;

    @SneakyThrows
    public DepartmentResponse execute(String id){
        var x= departmentELRepository.findById(id);
        if(x.isPresent()){
            return departmentMapper.toResponse(x.get());
        }else{
            throw new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED);
        }
    }
}
