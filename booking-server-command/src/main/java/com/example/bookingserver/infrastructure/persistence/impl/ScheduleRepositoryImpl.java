package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.persistence.repository.ScheduleJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleRepositoryImpl implements ScheduleRepository {

    ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleJpaRepository.save(schedule);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleJpaRepository.delete(schedule);
    }

    @Override
    public void delete(String id) {
        Optional<Schedule> schedule = scheduleJpaRepository.findById(id);
        if(schedule.isPresent()) {
            schedule.get().setStatusId(ApplicationConstant.Status.CANCELLED);
            scheduleJpaRepository.save(schedule.get());
        }
    }

    @Override
    public Optional<Schedule> findById(String id) {
        return scheduleJpaRepository.findById(id);
    }

    @Override
    public int getCountPersonPerDay(String doctorId, LocalDateTime start, LocalDateTime end) {
        return scheduleJpaRepository.getCountByDoctor(doctorId, start, end);
    }

    @Override
    public Page<Schedule> findByPatient(String patientId, Pageable pageable, int statusId) {
        return statusId == 0 ? scheduleJpaRepository.findByPatientId(patientId, pageable) : scheduleJpaRepository.findByPatientIdAndStatus(patientId, pageable, statusId) ;
    }

    @Override
    public Page<Schedule> findByDoctor(String doctorId, int statusId, Pageable pageable, LocalDateTime start, LocalDateTime end) {
        return  scheduleJpaRepository.findByDoctor(doctorId, statusId, pageable, start, end);
    }
}
