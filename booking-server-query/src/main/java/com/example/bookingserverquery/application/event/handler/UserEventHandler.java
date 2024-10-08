package com.example.bookingserverquery.application.event.handler;


import com.example.bookingserverquery.application.event.models.user.CreateUserEvent;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventHandler {

    final UserELRepository userELRepository;
    final UserMapper userMapper;
    final ObjectMapper objectMapper;


    @KafkaListener(topics = "create-user-event")
    @SneakyThrows
    void createUserEvent(String event){
        log.info("create-user-event: " + event);
        CreateUserEvent createUserEvent= objectMapper.readValue(event, CreateUserEvent.class);
        User user= userMapper.toUserFromCreateUserEvent(createUserEvent);
        userELRepository.save(user);
    }





}
