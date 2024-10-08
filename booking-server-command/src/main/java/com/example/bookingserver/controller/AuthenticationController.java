package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.user.SignInCommand;
import com.example.bookingserver.application.reponse.ApiResponse;
import com.example.bookingserver.application.service.SignInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    final SignInService signInService;

    @PostMapping("/sign-in")
    public ApiResponse signIn(@RequestBody @Valid SignInCommand command){
        var response= signInService.execute(command);
        log.info("Sign-In");
        return ApiResponse.success(200, "Đăng nhập thành công", response);
    }
}
