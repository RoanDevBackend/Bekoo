package com.example.bookingserver.application.command.user;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInCommand {
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    String phoneNumber;
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    String password;
}
