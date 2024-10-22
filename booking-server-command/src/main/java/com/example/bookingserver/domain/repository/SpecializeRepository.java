package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.Specialize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecializeRepository {
    Specialize save(Specialize specialize);
    void delete(String id);
    void delete(Specialize specialize);
    Optional<Specialize> findById(String id);
    Page<Specialize> findByDepartment(String departmentId, Pageable pageable);
    Page<Specialize> findAll(Pageable pageable);
}
