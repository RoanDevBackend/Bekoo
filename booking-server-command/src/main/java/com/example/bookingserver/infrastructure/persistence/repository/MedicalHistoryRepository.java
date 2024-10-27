package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.MedicalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    @Query("FROM MedicalHistory m " +
            "WHERE m.patient.id= :patientId ")
    Page<MedicalHistory> findAllByPatientId(String patientId, Pageable pageable);
}
