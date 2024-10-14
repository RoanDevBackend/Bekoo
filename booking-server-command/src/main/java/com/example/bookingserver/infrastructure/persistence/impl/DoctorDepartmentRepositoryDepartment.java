package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.DoctorDepartment;
import com.example.bookingserver.domain.idClass.DoctorDepartmentId;
import com.example.bookingserver.domain.repository.DoctorDepartmentRepository;
import com.example.bookingserver.infrastructure.persistence.repository.DoctorDepartmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DoctorDepartmentRepositoryDepartment implements DoctorDepartmentRepository {

    final DoctorDepartmentJpaRepository doctorDepartmentJpaRepository;

    @Override
    public DoctorDepartment save(DoctorDepartment doctorDepartment) {
        return doctorDepartmentJpaRepository.save(doctorDepartment);
    }

    @Override
    public Optional<DoctorDepartment> findById(String doctorId, String departmentId) {
        return doctorDepartmentJpaRepository.findByDoctorAndDepartment(doctorId, departmentId);
    }

    @Override
    public void delete(String doctorId, String departmentId) {
        Doctor doctor= new Doctor();
        doctor.setId(doctorId);

        Department department= new Department();
        department.setId(departmentId);
        DoctorDepartmentId doctorDepartmentId= new DoctorDepartmentId(doctor, department);
        doctorDepartmentJpaRepository.deleteById(doctorDepartmentId);
    }

    @Override
    public void delete(DoctorDepartment doctorDepartment) {
        doctorDepartmentJpaRepository.delete(doctorDepartment);
    }
}
