package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.infrastructure.persistence.repository.DepartmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {
    final DepartmentJpaRepository departmentJpaRepository;

    @Override
    public Department save(Department department) {
        return departmentJpaRepository.save(department);
    }

    @Override
    public Optional<Department> findById(String id) {
        return departmentJpaRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        departmentJpaRepository.deleteById(id);
    }

    @Override
    public void delete(Department department) {
        departmentJpaRepository.delete(department);
    }
}
