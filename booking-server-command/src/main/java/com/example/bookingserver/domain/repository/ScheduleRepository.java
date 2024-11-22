package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Schedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Schedule save(Schedule schedule);
    void delete(Schedule schedule);
    void delete(String id);
    Optional<Schedule> findById(String id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int getCountPersonPerDay(String doctorId, LocalDateTime start, LocalDateTime end);
    Page<Schedule> findByPatient(String patientId, Pageable pageable, int statusId);
    Page<Schedule> findByDoctor(String doctorId, int statusId, Pageable pageable, LocalDateTime start, LocalDateTime end);
}
