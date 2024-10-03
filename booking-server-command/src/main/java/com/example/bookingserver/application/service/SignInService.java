package com.example.bookingserver.application.service;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.reponse.UserResponse;

public interface SignInService {
    UserResponse execute(SignInCommand command);
}
