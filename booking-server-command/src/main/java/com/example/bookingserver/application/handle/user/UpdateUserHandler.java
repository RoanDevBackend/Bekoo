package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.UpdateInfoUserCommand;
import com.example.bookingserver.application.event.user.UpdateInfoUserEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserHandler implements Handler<UpdateInfoUserCommand> {

    final UserRepository userRepository;
    final UserMapper userMapper;

    @Override
    @SneakyThrows
    public void execute(UpdateInfoUserCommand command) {
        User user= userRepository.findById(command.getId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );

        userMapper.updateUser(user, command);
        userRepository.save(user);

        UpdateInfoUserEvent updateInfoUserEvent= userMapper.fromUserToUpdateUserEvent(user);

    }
}
