package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.user.*;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.user.CreateUserHandler;
import com.example.bookingserver.application.command.handle.user.DeleteUserHandler;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.domain.ERole;
import com.example.bookingserver.domain.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Slf4j
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    CreateUserHandler createUserHandler;
    Handler<UpdateInfoUserCommand> updateInfoUserCommandHandler;
    DeleteUserHandler deleteUserHandler;
    Handler<ChangePasswordCommand> changePasswordCommandHandler;
    Handler<ChangePasswordOTPCommand> changePasswordOTPCommandHandler;
    Handler<ForgotPasswordCommand> forgotPasswordCommandHandler;
    Handler<VerifyOTPCommand> verifyOTPCommandHandler;
    HandlerDTO<UpdateAvatarUserCommand, String> updateAvatarUserCommandHandler;

    @PostMapping()
    public ApiResponse createUser(@RequestBody @Valid CreateUserCommand command){
        Set<Role> roles= new HashSet<>();
        roles.add(new Role(ERole.USER));
        var response= createUserHandler.execute(command, roles);
        return ApiResponse.success(201, "Tạo tài khoản thành công", response);
    }


    @PutMapping()
    @SecurityRequirement(name="bearerAuth")
    public ApiResponse updateUser(@RequestBody @Valid UpdateInfoUserCommand command){
        updateInfoUserCommandHandler.execute(command);
        return ApiResponse.success(200, "Cập nhập thông tin tài khoản thành công");
    }


    @Operation(summary = "Thay đổi mật khẩu người dùng, có yêu cầu nhập mật khẩu cũ")
    @PutMapping("/password")
    @SecurityRequirement(name="bearerAuth")
    public ApiResponse changePassword(@RequestBody @Valid ChangePasswordCommand command){
        changePasswordCommandHandler.execute(command);
        return ApiResponse.success(200, "Thay đổi mật khẩu thành công");
    }


    @PutMapping("/password/otp")
    @Operation(summary = "Thay đổi mật khẩu người dùng, không yêu cầu nhập mật khẩu cũ")
    public ApiResponse changePasswordOTP(@RequestBody @Valid ChangePasswordOTPCommand command){
        changePasswordOTPCommandHandler.execute(command);
        return ApiResponse.success(200, "Thay đổi mật khẩu thành công");
    }


    @PostMapping("/avatar")
    @SecurityRequirement(name="bearerAuth")
    public ApiResponse updateAvatar(@ModelAttribute UpdateAvatarUserCommand command){
        String data= updateAvatarUserCommandHandler.execute(command);
        return ApiResponse.success(200, "Thay đổi ảnh đại diện thành công", data);
    }


    @PostMapping("/forgot-password/send-otp")
    public ApiResponse forgotPassword(@RequestBody @Valid ForgotPasswordCommand command){
        forgotPasswordCommandHandler.execute(command);
        return ApiResponse.success(200, "Gửi mã xác thực thành công, mã xác thực gồm 6 số");
    }



    @PostMapping("/forgot-password/verify")
    public ApiResponse verifyOTP(@RequestBody @Valid VerifyOTPCommand command){
        verifyOTPCommandHandler.execute(command);
        return ApiResponse.success(200, "Xác thực thành công");
    }


    @DeleteMapping("/{ids}")
    @SecurityRequirement(name="bearerAuth")
    public ApiResponse deleteUser(@PathVariable("ids") List<String> ids){
        deleteUserHandler.execute(ids);
        return ApiResponse.success(200, "Xoá tài khoản thành công");
    }
}
