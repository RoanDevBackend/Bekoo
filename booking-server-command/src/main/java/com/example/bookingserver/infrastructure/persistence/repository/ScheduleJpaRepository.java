package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Schedule;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<Schedule, String> {

    /***
     *
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
            "WHERE s.checkIn >= :start " +
            "AND s.checkIn < :end " +
            "AND s.statusId= 1 ")
    int getTotalValue(LocalDateTime start, LocalDateTime end);


    /**
     *
     Tất cả lịch khám của bác sĩ
     * @param id
     * @param pageable
     * @return
     */
    @Query("FROM Schedule s " +
            "WHERE s.patient.id= :id ")
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
            "AND s.checkIn < :end " )
    Page<Schedule> findByDoctor(String doctorId, Pageable pageable, LocalDateTime start, LocalDateTime end);
}
