package com.example.bookingserver.application.service;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.reponse.TokenResponse;
import com.example.bookingserver.application.reponse.UserResponse;

public interface AuthenticationService {
    TokenResponse signIn(SignInCommand command);
    UserResponse getIdByToken(String token);
}
