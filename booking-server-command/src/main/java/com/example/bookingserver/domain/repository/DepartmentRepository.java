package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository {
    Department save(Department department);

    Optional<Department> findById(String id);

    void delete(String id);

    void delete(Department department);
}
