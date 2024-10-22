package com.example.bookingserver.application.command.service;

import com.example.bookingserver.application.command.command.user.SignInCommand;
import com.example.bookingserver.application.command.reponse.TokenResponse;
import com.example.bookingserver.application.command.reponse.UserResponse;

public interface AuthenticationService {
    TokenResponse signIn(SignInCommand command);
    UserResponse getIdByToken(String token);
}
