package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.DoctorDepartment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorDepartmentRepository {
    DoctorDepartment save(DoctorDepartment doctorDepartment);

    Optional<DoctorDepartment> findById(String doctorId, String departmentId);

    void delete(String doctorId, String departmentId);

    void delete(DoctorDepartment doctorDepartment);
}
