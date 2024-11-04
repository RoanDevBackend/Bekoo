package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.UserResponse;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.application.command.service.PasswordService;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.application.command.command.user.CreateUserCommand;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.user.CreateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateUserHandler{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageProducer messageProducer;
    private final PasswordService passwordService;
    final String TOPIC= TopicConstant.UserTopic.CREATE_USER;

    @SneakyThrows
    public UserResponse execute(CreateUserCommand command, Set<Role> roles){
        if(userRepository.isEmailExisted(command.getEmail())){
            throw new BookingCareException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }
        if(!command.getPassword().equals(command.getConfirmPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }

        User user= userMapper.toUserFromCreateCommand(command);
        user.setPassword(passwordService.encode(user.getPassword()));
        user.setRoles(roles);

        user= userRepository.save(user);

        CreateUserEvent event= userMapper.fromUserToCreateUserEvent(user);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.ADD, event, event.getId(), "User");
        return userMapper.toResponse(user);
    }
}
