package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, String> {
    @Query("FROM Doctor d " +
            "WHERE d.user.id= :userId ")
    Optional<Doctor> findByUser(String userId);
}
