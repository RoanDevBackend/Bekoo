package com.example.bookingserver.application.service.impl;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.mapper.UserMapper;
import com.example.bookingserver.application.reponse.UserResponse;
import com.example.bookingserver.application.service.PasswordService;
import com.example.bookingserver.application.service.SignInService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordService passwordService;
    @Override
    @SneakyThrows
    public UserResponse execute(SignInCommand command) {
        passwordService.encode(command.getPassword());
        User user= userRepository.signIn(command.getEmail(), command.getPassword())
                .orElseThrow(()-> new BookingCareException(ErrorDetail.ERR_USER_UN_AUTHENTICATE));

        return userMapper.toResponse(user);
    }
}
