package com.example.bookingserver.application.command.handle.specialize;


import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeleteSpecializeHandler {

    SpecializeRepository specializeRepository;
    ObjectMapper objectMapper;
    String TOPIC="delete-specialize";
    MessageProducer messageProducer;
    OutboxEventRepository outboxEventRepository;


    @SneakyThrows
    @Transactional
    public void execute(List<String> ids){
        for(String id: ids){
            Optional<Specialize> specializeOptional= specializeRepository.findById(id);
            if(specializeOptional.isPresent()) {
                specializeRepository.delete(id);
                String event= id;
                String content= objectMapper.writeValueAsString(event);
                OutboxEvent outboxEvent= OutboxEvent.builder()
                        .topic(TOPIC)
                        .eventType("DELETE-SPECIALIZE")
                        .aggregateId(id)
                        .aggregateType("Specialize")
                        .content(content)
                        .status(ApplicationConstant.EventStatus.PENDING)
                        .build();

                try {
                    messageProducer.sendMessage(TOPIC, content);
                    outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
                }catch (Exception e){
                    log.error("SEND EVENT CREATE SPECIALIZE FAILED: {}", TOPIC );
                }
                outboxEventRepository.save(outboxEvent);
            }
            else{
                log.error("SPECIALIZE NOT FOUND: {}", id);
            }
        }
    }
}
