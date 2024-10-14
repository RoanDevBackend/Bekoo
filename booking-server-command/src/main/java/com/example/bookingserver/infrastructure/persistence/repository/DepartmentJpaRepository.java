package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentJpaRepository extends JpaRepository<Department, String> {
}
