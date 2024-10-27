package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.EmergencyContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    @Query("FROM EmergencyContact ec " +
            "WHERE ec.patient.id= :patientId ")
    Page<EmergencyContact> findAllByPatientId(String patientId, Pageable pageable);

    @Query("SELECT COUNT(*) " +
            "FROM EmergencyContact ec " +
            "WHERE ec.patient.id= :patientId ")
    Integer countPatient(String patientId);
}
