package com.example.bookingserver.infrastructure.persistence.impl;

import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.persistence.repository.OutboxEventJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventRepositoryImpl implements OutboxEventRepository {

    final OutboxEventJPARepository outboxEventJPARepository;

    @Override
    public List<OutboxEvent> findAllByStatus(String status) {
        return outboxEventJPARepository.findAllByStatus(status);
    }

    @Override
    public void save(OutboxEvent outboxEvent) {
        outboxEventJPARepository.save(outboxEvent);
    }
}
