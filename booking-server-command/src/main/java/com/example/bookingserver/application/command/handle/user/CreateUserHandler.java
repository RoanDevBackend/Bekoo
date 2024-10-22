package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.event.user.CreateUserEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.UserResponse;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.application.command.service.PasswordService;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.application.command.command.user.CreateUserCommand;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateUserHandler{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final PasswordService passwordService;
    private final String TOPIC="create-user-event";

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

        String content= objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("CREATE")
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
        return userMapper.toResponse(user);
    }
}
