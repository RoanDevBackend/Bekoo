package com.example.bookingserver.infrastructure.persistence.repository;

import com.example.bookingserver.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventJPARepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findAllByStatus(String status);
}
