package com.example.bookingserverquery.domain.repository;

import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository {
    Page<Doctor> findByName(String name, Pageable pageable);
    Page<Doctor> getAll(Pageable pageable);
    Optional<Doctor> findById(String id);
}
