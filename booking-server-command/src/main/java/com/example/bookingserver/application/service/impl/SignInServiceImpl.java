package com.example.bookingserver.application.service.impl;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.infrastructure.mapper.UserMapper;

import com.example.bookingserver.application.reponse.TokenResponse;
import com.example.bookingserver.application.service.JwtService;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.application.service.PasswordService;
import com.example.bookingserver.application.service.SignInService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordService passwordService;
    final AuthenticationManager authenticationManager;
    final JwtService jwtService;

    private final Long TIME_TOKEN=1000*60*60*24L; // 1 ngay
    private final Long TIME_REFRESH_TOKEN=100*60*60*24*2L;//2 ngay
    @Override
    @SneakyThrows
    public TokenResponse execute(SignInCommand command) {
        User user = userRepository.findByUserName(command.getEmail());

        if (user == null)
        {
            throw new BookingCareException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    command.getEmail(),
                    command.getPassword()
            ));
        }catch (Exception e){
            throw new BookingCareException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }

        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        var refreshToken = jwtService.generateToken(user, TIME_REFRESH_TOKEN);
        Set<String> roles= new HashSet<>() ;
        for(Role x : user.getRoles()){
            roles.add(x.getName()) ;
        }
        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(command.getEmail())
                .roleName(roles)
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_REFRESH_TOKEN))
                .build();
    }
}
