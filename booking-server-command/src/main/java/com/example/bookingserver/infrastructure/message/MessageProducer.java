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
        this.sendMessage(topic, null, value, null, null);
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
            kafkaTemplate.send(topic, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
            String contentLog = "SEND EVENT SUCCESS WITH TOPIC: '" + topic + "' VALUE: " + content;
            log.info(contentLog);
        }catch (Exception e){
            String contentLog = "SEND EVENT ERROR WITH TOPIC: '" + topic + "' VALUE: " + content;
            log.info(contentLog);
        }
        outboxEventRepository.save(outboxEvent);
    }
}
