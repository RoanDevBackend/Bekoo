package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.user.SignInCommand;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ApiResponse signIn(@RequestBody @Valid SignInCommand command){
        var response= authenticationService.signIn(command);
        return ApiResponse.success(200, "Đăng nhập thành công", response);
    }

    @PostMapping("/token/refresh/{token}")
    public ApiResponse refreshToken(@PathVariable String token){
        var response= authenticationService.refreshToken(token);
        return ApiResponse.success(200, "Refresh token", response);
    }

    @Operation(summary = "Lấy ra user từ token", description = "Cần token ở header")
    @PostMapping("/token")
    public ApiResponse getUserByToken(HttpServletRequest request){
        var token= request.getHeader(HttpHeaders.AUTHORIZATION);
        var response= authenticationService.getIdByToken(token);
        return ApiResponse.success(200, "Thành công", response);
    }
}


