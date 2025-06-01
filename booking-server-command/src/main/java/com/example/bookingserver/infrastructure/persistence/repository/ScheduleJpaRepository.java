package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, String> {

    /***
     Lấy ra số người đã đăng kí khám theo ngày của bác sĩ
     * @param doctorId
     * @param start
     * @param end
     * @return
     */
    @Query("SELECT COUNT(*) " +
            "FROM Schedule s " +
            "WHERE s.doctor.id= :doctorId " +
            "AND s.checkIn >= :start " +
            "AND s.checkIn < :end " +
            "AND s.statusId= 1 ")
    int getCountByDoctor(String doctorId, LocalDateTime start, LocalDateTime end);

    /***
     *
     Lấy ra số người đã đăng kí khám theo ngày của hệ thống
     * @param start
     * @param end
     * @return
     */
    @Query("SELECT COUNT(*) " +
            "FROM Schedule s " +
            "WHERE s.createdAt >= :start " +
            "AND s.createdAt < :end " +
            "AND s.statusId= 1 ")
    Integer getTotalValue(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(s.specialize.price) " +
            "FROM Schedule s " +
            "WHERE s.createdAt >= :start " +
            "AND s.createdAt < :end " +
            "AND s.statusId= 3 "
     )
    Integer getTotalPrice(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(*) " +
            "FROM Schedule s " +
            "WHERE s.doctor.id= :doctorId " +
            "AND s.createdAt >= :start " +
            "AND s.createdAt < :end " +
            "AND s.statusId= 1 ")
    int getCountByDoctorAndCreatedAt(String doctorId, LocalDateTime start, LocalDateTime end);


    /**
     *
     Tất cả lịch khám của bác sĩ
     * @param id
     * @param pageable
     * @return
     */
    @Query("FROM Schedule s " +
            "WHERE s.patient.id= :id " +
            "AND s.statusId= :statusId ")
    Page<Schedule> findByPatientIdAndStatus(String id, Pageable pageable, int statusId);

    @Query("FROM Schedule s " +
            "WHERE s.patient.id= :id " )
    Page<Schedule> findByPatientId(String id, Pageable pageable);


    /**
     *
     Tất cả lịch khám của bác sĩ theo thời gian
     * @param doctorId
     * @param pageable
     * @param start
     * @param end
     * @return
     */
    @Query("FROM Schedule s " +
            "WHERE s.doctor.id= :doctorId " +
            "AND s.checkIn >= :start " +
            "AND s.checkIn < :end " +
            "AND (s.statusId= :statusId " +
            "OR :statusId = 0 )" )
    Page<Schedule> findByDoctor(String doctorId, int statusId, Pageable pageable, LocalDateTime start, LocalDateTime end);


    @Query("SELECT COUNT (*)  " +
            "FROM Schedule s " +
            "WHERE s.doctor.id = :doctorId " +
            "AND s.checkIn = :checkIn " +
            "AND s.statusId = 1 ")
    int countScheduleByDoctorIdAndCheckIn(String doctorId, LocalDateTime checkIn);
}
