package com.example.bookingserver.application.command.user;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyOTPCommand {
    @NotBlank(message = "Số điện thoại không được để trống")
    String phoneNumber;
    @NotBlank(message = "OTP không được để trống")
    int code;
}
