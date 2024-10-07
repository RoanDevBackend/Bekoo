package com.example.bookingserver.domain.repository;

import com.example.bookingserver.domain.OutboxEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository {
    List<OutboxEvent> findAllByStatus(String status);
    void save(OutboxEvent outboxEvent);
}
