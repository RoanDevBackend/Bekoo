package com.example.bookingserver.infrastructure.message;

import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    final ObjectMapper objectMapper;
    final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(String topic, String value){
        kafkaTemplate.send(topic, value);
        log.info("VALUE: {}", value);
    }

    @SneakyThrows
    public void sendMessage(String topic, String eventType, Object event, String aggregateId,String aggregateType){

        final String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(topic)
                .eventType(eventType)
                .aggregateId(aggregateId)
                .aggregateType(aggregateType)
                .content(content)
                .status(ApplicationConstant.EventStatus.PENDING)
                .build();

        try {
            this.sendMessage(topic, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
            log.info("SEND EVENT SUCCESS: {}", topic);
        }catch (Exception e){
            log.error("SEND EVENT FAILED: {}", topic);
        }
        outboxEventRepository.save(outboxEvent);
    }
}
