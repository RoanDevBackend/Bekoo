package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Doctor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository {
    Doctor save(Doctor doctor);
    Optional<Doctor> findById(String id);
    void delete(String id);
    void delete(Doctor doctor);
}
