package com.example.bookingserverquery.application.service.impl;

import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.application.service.i.DoctorService;
import com.example.bookingserverquery.domain.Department;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.DoctorDepartment;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.example.bookingserverquery.infrastructure.repository.ScheduleELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DoctorServiceImpl implements DoctorService {
    final ScheduleELRepository scheduleELRepository;
    final DoctorMapper doctorMapper;
    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final DepartmentELRepository departmentELRepository;

    @Override
    public Long getTotalPatientsVisited(String doctorId) {
        return scheduleELRepository.countByDoctorId(doctorId);
    }

    @Override
    public DoctorResponse toResponse(Doctor doctor) {

        List<String> departmentNames= new ArrayList<>();

        var doctorDepartments= doctorDepartmentELRepository.findByDoctorId(doctor.getId(), Pageable.unpaged());
        for(DoctorDepartment x: doctorDepartments){
            var department= departmentELRepository.findById(x.getDepartmentId());
            department.ifPresent(value -> departmentNames.add(value.getName()));
        }
        String departmentName="Thuộc khoa: ";
        if(departmentNames.isEmpty()){
            departmentName= "Bác sĩ này hiện chưa thuộc khoa nào";
        }
        for(int i =0 ;i < departmentNames.size(); i++){
            departmentName+=departmentNames.get(i);
            if(i != departmentNames.size()-1) {
                departmentName += ", ";
            }
        }
        Long totalPatientsVisited = getTotalPatientsVisited(doctor.getId());
        DoctorResponse doctorResponse= doctorMapper.toResponse(doctor);


        doctorResponse.setTotalPatientsVisited(totalPatientsVisited);
        doctorResponse.setDepartment(departmentName);
        return doctorResponse;
    }
}
