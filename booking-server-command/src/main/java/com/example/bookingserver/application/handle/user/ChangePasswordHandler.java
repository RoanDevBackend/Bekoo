package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.ChangePasswordCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.service.PasswordService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordHandler implements Handler<ChangePasswordCommand> {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    @SneakyThrows
    public void execute(ChangePasswordCommand command) {
        User user= userRepository.findById(command.getId()).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );

        passwordService.encode(command.getOldPassword());

        if(!command.getOldPassword().equals(user.getPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CORRECT);
        }

        if(!command.getNewPassword().equals(command.getConfirmPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }

        passwordService.encode(command.getNewPassword());
        user.setPassword(command.getNewPassword());
        userRepository.save(user);
    }

}
