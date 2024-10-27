package com.example.bookingserverquery.application.event.handler;


import com.example.bookingserverquery.application.event.models.specialize.SpecializeEvent;
import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecializeEventHandler {
    SpecializeELRepository specializeELRepository;
    SpecializeMapper specializeMapper;
    ObjectMapper objectMapper;

    @KafkaListener(topics = {"create-specialize", "update-specialize"})
    @SneakyThrows
    public void createOrUpdate(String event){
        SpecializeEvent specializeEvent = objectMapper.readValue(event, SpecializeEvent.class);
        Specialize specialize= specializeMapper.toSpecialize(specializeEvent);
        specializeELRepository.save(specialize);
        log.info("Event Create Or Update {} SUCCESS", event);
    }


    @KafkaListener(topics = "delete-specialize")
    public void delete(String event){
        String id= objectMapper.convertValue(event, String.class);
        specializeELRepository.deleteById(id);
    }
}
