package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.DoctorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DoctorRepositoryImpl implements DoctorRepository{

    final DoctorJpaRepository doctorJpaRepository;

    @Override
    public Doctor save(Doctor doctor) {
        return doctorJpaRepository.save(doctor);
    }

    @Override
    public Optional<Doctor> findById(String id) {
        return doctorJpaRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        var doctor= doctorJpaRepository.findById(id);
        if(doctor.isPresent()) {
            doctor.get().setUser(null);
            doctorJpaRepository.delete(doctor.get());
        }
    }

    @Override
    public void delete(Doctor doctor) {
        doctorJpaRepository.delete(doctor);
    }
}
