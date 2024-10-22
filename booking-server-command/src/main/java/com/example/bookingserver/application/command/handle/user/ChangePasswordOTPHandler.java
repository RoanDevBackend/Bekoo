package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.command.user.ChangePasswordOTPCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.service.PasswordService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordOTPHandler implements Handler<ChangePasswordOTPCommand> {

    final UserRepository userRepository;
    final PasswordService passwordService;

    @Override
    @SneakyThrows
    public void execute(ChangePasswordOTPCommand command) {
        if(!command.getNewPassword().equals(command.getConfirmPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }
        User user= userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        String newPassword= passwordService.encode(command.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
