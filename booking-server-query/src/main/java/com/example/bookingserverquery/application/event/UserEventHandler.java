package com.example.bookingserverquery.application.event;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import document.event.user.CreateUserEvent;
import document.event.user.DeleteUserEvent;
import document.event.user.UpdateAvatarUserEvent;
import document.event.user.UpdateInfoUserEvent;
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


    @KafkaListener(topics = TopicConstant.UserTopic.CREATE_USER)
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

    @KafkaListener(topics = TopicConstant.UserTopic.DELETE_USER)
    @SneakyThrows
    void deleteUser(String event){
        DeleteUserEvent events= objectMapper.readValue(event, DeleteUserEvent.class);
        for(String id: events.getIds()) {
            userELRepository.deleteById(id);
        }
        log.info("DELETE USER SUCCESS: {}", event);
    }

    @KafkaListener(topics = TopicConstant.UserTopic.UPDATE_INFO_USER)
    @SneakyThrows
    void updateInfoUser(String event){
        UpdateInfoUserEvent updateInfoUserEvent= objectMapper.readValue(event, UpdateInfoUserEvent.class);
        User user= userELRepository.findById(updateInfoUserEvent.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        userMapper.updateInfo(user, updateInfoUserEvent);
        userELRepository.save(user);
        log.info("UPDATE INFO USER SUCCESS: {}", event);
    }

    @KafkaListener(topics = TopicConstant.UserTopic.UPDATE_AVATAR_USER)
    @SneakyThrows
    void updateAvatar(String event){
        UpdateAvatarUserEvent updateAvatarUserEvent= objectMapper.readValue(event, UpdateAvatarUserEvent.class);
        User user= userELRepository.findById(updateAvatarUserEvent.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        user.setLinkAvatar(updateAvatarUserEvent.getLinkAvatar());
        userELRepository.save(user);
        log.info("UPDATE-AVATAR-USER SUCCESS: {}", event);
    }
}
