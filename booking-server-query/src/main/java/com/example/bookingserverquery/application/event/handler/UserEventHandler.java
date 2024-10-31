package com.example.bookingserverquery.application.event.handler;


import com.example.bookingserverquery.application.event.models.user.CreateUserEvent;
import com.example.bookingserverquery.application.event.models.user.DeleteUserEvent;
import com.example.bookingserverquery.application.event.models.user.UpdateAvatarUserEvent;
import com.example.bookingserverquery.application.event.models.user.UpdateInfoUserEvent;
import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;

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
        try {
            CreateUserEvent createUserEvent = objectMapper.readValue(event, CreateUserEvent.class);
            log.info(createUserEvent.toString());
            User user = userMapper.toUserFromCreateUserEvent(createUserEvent);
            userELRepository.save(user);
            log.info("CREATE-USER-EVENT SUCCESS: {}", event);
        }catch (Exception e){
            log.error("CREATE-USER-EVENT ERROR: {}", e.getMessage());
        }
    }


    @KafkaListener(topics = "delete-user-event")
    @SneakyThrows
    void deleteUser(String event){
        log.info("delete-user-event: " + event);
        DeleteUserEvent events= objectMapper.readValue(event, DeleteUserEvent.class);
        for(String id: events.getIds()) {
            userELRepository.deleteById(id);
        }
    }



    @KafkaListener(topics = "update-info-user-event")
    @SneakyThrows
    void updateInfoUser(String event){
        log.info("update-info-user-event: " + event);
        UpdateInfoUserEvent updateInfoUserEvent= objectMapper.readValue(event, UpdateInfoUserEvent.class);
        User user= userELRepository.findById(updateInfoUserEvent.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        userMapper.updateInfo(user, updateInfoUserEvent);
        userELRepository.save(user);
    }



    @KafkaListener(topics = "update-avatar-user-event")
    @SneakyThrows
    void updateAvatar(String event){
        log.info("update-avatar-user-event: " + event);
        UpdateAvatarUserEvent updateAvatarUserEvent= objectMapper.readValue(event, UpdateAvatarUserEvent.class);
        User user= userELRepository.findById(updateAvatarUserEvent.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        user.setLinkAvatar(updateAvatarUserEvent.getLinkAvatar());
        userELRepository.save(user);
    }






}
