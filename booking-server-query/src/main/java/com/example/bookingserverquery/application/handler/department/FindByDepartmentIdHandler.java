package com.example.bookingserverquery.application.handler.department;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByDepartmentIdHandler {

    final DepartmentELRepository departmentELRepository;
    final SpecializeELRepository specializeELRepository;
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final DepartmentMapper departmentMapper;

    @SneakyThrows
    public DepartmentResponse execute(String id){
        var x= departmentELRepository.findById(id);
        if(x.isPresent()){
            DepartmentResponse departmentResponse= departmentMapper.toResponse(x.get());
            Integer totalDoctor= doctorDepartmentELRepository.countByDepartmentId(x.get().getId());
            Long totalSpecialize = specializeELRepository.countByDepartment(x.get().getId());
            departmentResponse.setTotalDoctor(totalDoctor);
            departmentResponse.setTotalSpecialize(totalSpecialize);
            return departmentResponse;
        }else{
            throw new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED);
        }
    }
}
