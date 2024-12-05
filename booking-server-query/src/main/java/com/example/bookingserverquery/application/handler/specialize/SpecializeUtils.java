package com.example.bookingserverquery.application.handler.specialize;

import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.ScheduleELRepository;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.DepartmentResponse;
import document.response.SpecializeResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecializeUtils {

    final SpecializeMapper specializeMapper;
    final ScheduleELRepository scheduleELRepository;
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final SpecializeELRepository specializeELRepository;

    public SpecializeResponse convertToResponse(Specialize specialize) {
        SpecializeResponse specializeResponse = specializeMapper.toResponse(specialize);
        long totalPatient= scheduleELRepository.countBySpecializedId(specialize.getId());
        specializeResponse.setTotalPatient(totalPatient);

        DepartmentResponse departmentResponse = specializeResponse.getDepartment();
        long totalDoctor = doctorDepartmentELRepository.findByDepartmentId(departmentResponse.getId(), Pageable.unpaged()).getTotalElements();
        departmentResponse.setTotalSpecialize(specializeELRepository.countByDepartment(departmentResponse.getId()));
        departmentResponse.setTotalDoctor((int) totalDoctor);

        specializeResponse.setDepartment(departmentResponse);
        return specializeResponse;
    }

    public List<SpecializeResponse> convertToResponseList(List<Specialize> specializes) {
        List<SpecializeResponse> contents= new ArrayList<>();
        for(Specialize specialize: specializes){
            var response= this.convertToResponse(specialize);
            contents.add(response);
        }
        return contents;
    }
}
