package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.DoctorDepartment;
import com.example.bookingserver.domain.idClass.DoctorDepartmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorDepartmentJpaRepository extends JpaRepository<DoctorDepartment, DoctorDepartmentId> {

    @Query("FROM DoctorDepartment dp " +
            "WHERE dp.doctor.id= :doctorId " +
            "AND dp.department.id= :departmentId ")
    Optional<DoctorDepartment> findByDoctorAndDepartment(String doctorId, String departmentId);
}
