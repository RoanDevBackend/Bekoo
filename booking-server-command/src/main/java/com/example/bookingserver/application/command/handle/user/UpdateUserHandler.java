package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.command.user.UpdateInfoUserCommand;
import com.example.bookingserver.application.command.event.user.UpdateInfoUserEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateUserHandler implements Handler<UpdateInfoUserCommand> {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.UserTopic.UPDATE_INFO_USER;

    @Override
    @SneakyThrows
    public void execute(UpdateInfoUserCommand command) {
        User user= userRepository.findById(command.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );

        userMapper.updateUser(user, command);
        userRepository.save(user);

        UpdateInfoUserEvent event= userMapper.fromUserToUpdateUserEvent(user);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.UPDATE, event, event.getId(), "User");
    }
}
