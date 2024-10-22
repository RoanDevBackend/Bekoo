package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.command.user.SignInCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.UserResponse;
import com.example.bookingserver.infrastructure.mapper.UserMapper;

import com.example.bookingserver.application.command.reponse.TokenResponse;
import com.example.bookingserver.application.command.service.JwtService;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.application.command.service.PasswordService;
import com.example.bookingserver.application.command.service.AuthenticationService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
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
public class AuthenticationServiceImpl implements AuthenticationService {

    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordService passwordService;
    final AuthenticationManager authenticationManager;
    final JwtService jwtService;

    private final Long TIME_TOKEN=1000*60*60*24L; // 1 ngay
    private final Long TIME_REFRESH_TOKEN=100*60*60*24*2L;//2 ngay
    @Override
    @SneakyThrows
    public TokenResponse signIn(SignInCommand command) {

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

    @Override
    @SneakyThrows
    public UserResponse getIdByToken(String token) {
        token= token.substring(7);
        String username= jwtService.extractUsername(token);
        User user= userRepository.findByUserName(username);
        if(user == null) throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);
        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    @SneakyThrows
    public TokenResponse refreshToken(String refreshToken) {
        if(jwtService.isTokenExpired(refreshToken)){
            throw new BookingCareException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }
        String username= jwtService.extractUsername(refreshToken);
        User user= userRepository.findByUserName(username);
        if(user == null) throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);

        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        refreshToken = jwtService.generateToken(user, TIME_REFRESH_TOKEN);
        Set<String> roles= new HashSet<>() ;
        for(Role x : user.getRoles()){
            roles.add(x.getName()) ;
        }
        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(username)
                .roleName(roles)
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_REFRESH_TOKEN))
                .build();
    }


}
