package com.example.bookingserver.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendMessage(String topic, Object value){
        kafkaTemplate.send(topic, value);
        log.info("Message Producer: Send " + topic + " Success");
    }
}
