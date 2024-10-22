package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.command.user.UpdateInfoUserCommand;
import com.example.bookingserver.application.command.event.user.UpdateInfoUserEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserHandler implements Handler<UpdateInfoUserCommand> {

    final UserRepository userRepository;
    final ObjectMapper objectMapper;
    final UserMapper userMapper;
    final MessageProducer messageProducer;
    final OutboxEventRepository outboxEventRepository;
    final String TOPIC= "update-info-user-event";

    @Override
    @SneakyThrows
    public void execute(UpdateInfoUserCommand command) {
        User user= userRepository.findById(command.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );

        userMapper.updateUser(user, command);
        userRepository.save(user);

        UpdateInfoUserEvent event= userMapper.fromUserToUpdateUserEvent(user);
        String content= objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("UPDATE")
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

    }
}
