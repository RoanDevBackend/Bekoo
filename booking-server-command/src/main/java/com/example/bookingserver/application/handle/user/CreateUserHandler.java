package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.event.CreateUserEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.mapper.UserMapper;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.application.command.user.CreateUserCommand;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserHandler implements Handler<CreateUserCommand> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @SneakyThrows
    public void execute(CreateUserCommand command){

        if(userRepository.isPhoneNumberExisted(command.getPhoneNumber())){
            throw new BookingCareException(ErrorDetail.ERR_USER_PHONE_NUMBER_EXISTED);
        }

        if(!command.getPassword().equals(command.getConfirmPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }

        User user= userMapper.toUserFromCreateCommand(command);
        user= userRepository.save(user);

    }
}
