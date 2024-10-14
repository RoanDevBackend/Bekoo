package com.example.bookingserverquery.infrastructure.repository.impl;

import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.repository.DoctorRepository;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DoctorRepositoryImpl implements DoctorRepository {

    final DoctorELRepository doctorELRepository;


    @Override
    public Page<Doctor> findByName(String name, Pageable pageable) {
        return doctorELRepository.findDoctorsByName(name, pageable);
    }

    @Override
    public Page<Doctor> getAll(Pageable pageable) {
        return doctorELRepository.findAll(pageable);
    }

    @Override
    public Optional<Doctor> findById(String id) {
        return doctorELRepository.findById(id);
    }
}
