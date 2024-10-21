package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.Specialize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializeJpaRepository extends JpaRepository<Specialize, String> {
    @Query("FROM Specialize s " +
            "WHERE s.department.id= :id ")
    Page<Specialize> findByDepartmentId(String id, Pageable pageable);
}
