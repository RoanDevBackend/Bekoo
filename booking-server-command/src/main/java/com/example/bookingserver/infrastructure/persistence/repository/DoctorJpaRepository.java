package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, String> {
}
