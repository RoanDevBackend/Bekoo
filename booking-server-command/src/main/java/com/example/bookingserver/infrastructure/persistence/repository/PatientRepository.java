package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    @Query("FROM Patient p " +
            "WHERE p.user.id= :id ")
    Optional<Patient> findByUserId(String id);
}
