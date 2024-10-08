package com.example.bookingserver.application.handle.user;

import com.cloudinary.Cloudinary;
import com.example.bookingserver.application.command.user.UpdateAvatarUserCommand;
import com.example.bookingserver.application.event.user.CreateUserEvent;
import com.example.bookingserver.application.event.user.UpdateAvatarUserEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateAvatarUseHandler implements Handler<UpdateAvatarUserCommand> {

    final Cloudinary cloudinary;
    final UserRepository userRepository;
    final ObjectMapper objectMapper;
    final UserMapper userMapper;
    final MessageProducer messageProducer;
    final OutboxEventRepository outboxEventRepository;
    final String TOPIC= "update-avatar-user-event";
    @Override
    @SneakyThrows
    public void execute(UpdateAvatarUserCommand command) {
        User user= userRepository.findById(command.getId())
                .orElseThrow(()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        try {
            Map responseByCloudinary = cloudinary.uploader().upload(command.getFileImage().getBytes(), Map.of());
            user.setLinkAvatar(responseByCloudinary.get("url") + "");
            userRepository.save(user);

            UpdateAvatarUserEvent event= userMapper.fromUserToUpdateAvatarEvent(user);
            String content= objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent= OutboxEvent.builder()
                    .topic(TOPIC)
                    .eventType("UPDATE-AVATAR")
                    .aggregateId(user.getId())
                    .aggregateType("User")
                    .content(content)
                    .status(ApplicationConstant.EventStatus.PENDING)
                    .build();

            try {
                messageProducer.sendMessage(TOPIC, content);
                outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
                log.info("SEND EVENT SUCCESS: {}", TOPIC);
            }catch (Exception e){
                log.error("SEND EVENT FAILED: {}", TOPIC );
            }
            outboxEventRepository.save(outboxEvent);



        }catch (IOException e){
            log.error("ERR_FILE: " + e.getMessage());
            throw new BookingCareException(ErrorDetail.ERR_FILE);
        }
    }
}
