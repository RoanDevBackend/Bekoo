package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.reponse.ApiResponse;
import com.example.bookingserver.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
        log.info("Sign-In");
        return ApiResponse.success(200, "Đăng nhập thành công", response);
    }

    @Operation(summary = "Lấy ra user từ token", description = "Cần token ở header")
    @PostMapping("/token")
    public ApiResponse getUserByToken(HttpServletRequest request){
        var token= request.getHeader(HttpHeaders.AUTHORIZATION);
        var response= authenticationService.getIdByToken(token);
        return ApiResponse.success(200, "Thành công", response);
    }
}


