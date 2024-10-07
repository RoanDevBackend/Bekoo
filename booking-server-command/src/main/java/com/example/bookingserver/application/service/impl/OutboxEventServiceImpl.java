package com.example.bookingserver.application.service.impl;

import com.example.bookingserver.application.service.OutboxEventService;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventServiceImpl implements OutboxEventService {

    final OutboxEventRepository outboxEventRepository;
    final MessageProducer messageProducer;

    @Override
    public void execute() {
        List<OutboxEvent> outboxEvents= outboxEventRepository.findAllByStatus(ApplicationConstant.EventStatus.PENDING);

        for(OutboxEvent x: outboxEvents) {
            try {
                messageProducer.sendMessage(x.getTopic(), x.getContent());
                x.setStatus(ApplicationConstant.EventStatus.SEND);
                outboxEventRepository.save(x);
                log.info("SEND MESSAGE SUCCESS: {}", x.getTopic());
            }catch (Exception e){
                log.error("Send message failure: {}", x.getTopic());
                log.error(e.getMessage());
            }
        }
    }
}
