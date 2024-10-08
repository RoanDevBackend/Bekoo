package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.user.*;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.reponse.ApiResponse;
import com.example.bookingserver.application.service.SignInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    final Handler<CreateUserCommand> createUserHandler;
    final Handler<UpdateInfoUserCommand> updateInfoUserCommandHandler;
    final Handler<DeleteUserCommand> deleteUserCommandHandler;
    final Handler<ChangePasswordCommand> changePasswordCommandHandler;
    final Handler<ChangePasswordOTPCommand> changePasswordOTPCommandHandler;
    final Handler<ForgotPasswordCommand> forgotPasswordCommandHandler;
    final Handler<VerifyOTPCommand> verifyOTPCommandHandler;
    final Handler<UpdateAvatarUserCommand> updateAvatarUserCommandHandler;


    @PostMapping()
    public ApiResponse createUser(@RequestBody @Valid CreateUserCommand command){
        createUserHandler.execute(command);
        log.info("Create User: " + command.getName());
        return ApiResponse.success(201, "Tạo tài khoản thành công");
    }


    @PutMapping()
    public ApiResponse updateUser(@RequestBody @Valid UpdateInfoUserCommand command){
        updateInfoUserCommandHandler.execute(command);
        log.info("Update User: " + command.getId());
        return ApiResponse.success(200, "Cập nhập thông tin tài khoản thành công");
    }


    @PutMapping("/password")
    public ApiResponse changePassword(@RequestBody @Valid ChangePasswordCommand command){
        changePasswordCommandHandler.execute(command);
        log.info("Change Password: " + command.getId());
        return ApiResponse.success(200, "Thay đổi mật khẩu thành công");
    }


    @PutMapping("/password/otp")
    public ApiResponse changePasswordOTP(@RequestBody @Valid ChangePasswordOTPCommand command){
        changePasswordOTPCommandHandler.execute(command);
        log.info("Change password from OTP: {}", command.getEmail());
        return ApiResponse.success(200, "Thay đổi mật khẩu thành công");
    }


    @PutMapping("/avatar")
    public ApiResponse updateAvatar(@ModelAttribute UpdateAvatarUserCommand command){
        log.info("abcs");
        updateAvatarUserCommandHandler.execute(command);
        log.info("Update Avatar User: " + command.getId());
        return ApiResponse.success(200, "Thay đổi ảnh đại diện thành công");
    }


    @PostMapping("/forgot-password/send-otp")
    public ApiResponse forgotPassword(@RequestBody @Valid ForgotPasswordCommand command){
        forgotPasswordCommandHandler.execute(command);
        log.info("Send OTP for phone number: {}", command.getEmail());
        return ApiResponse.success(200, "Gửi mã xác thực thành công, mã xác thực gồm 6 số");
    }



    @PostMapping("/forgot-password/verify")
    public ApiResponse verifyOTP(@RequestBody @Valid VerifyOTPCommand command){
        verifyOTPCommandHandler.execute(command);
        log.info("Verify User: {}", command.getEmail());
        return ApiResponse.success(200, "Xác thực thành công");
    }


    @DeleteMapping()
    public ApiResponse deleteUser(@RequestBody DeleteUserCommand command){
        deleteUserCommandHandler.execute(command);
        log.info("Delete User: {}", command.toString() );
        return ApiResponse.success(200, "Xoá tài khoản thành công");
    }
}
