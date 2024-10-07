package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.event.user.CreateUserEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.application.service.PasswordService;
import com.example.bookingserver.domain.ERole;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.application.command.user.CreateUserCommand;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateUserHandler implements Handler<CreateUserCommand> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final PasswordService passwordService;
    private final String TOPIC="create-user-event";

    @Override
    @SneakyThrows
    public void execute(CreateUserCommand command){

        if(userRepository.isEmailExisted(command.getEmail())){
            throw new BookingCareException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }

        if(!command.getPassword().equals(command.getConfirmPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }


        User user= userMapper.toUserFromCreateCommand(command);
        user.setPassword(passwordService.encode(user.getPassword()));
        Set<Role> roles= new HashSet<>();
        roles.add(new Role(ERole.USER));

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
    }
}
