package com.example.bookingserver.application.command.service;

import com.example.bookingserver.application.command.command.user.SignInCommand;
import com.example.bookingserver.application.command.reponse.GetInfoByTokenResponse;
import com.example.bookingserver.application.command.reponse.TokenResponse;

public interface AuthenticationService {
    TokenResponse signIn(SignInCommand command);
    GetInfoByTokenResponse getIdByToken(String token);
    TokenResponse refreshToken(String token);
}
