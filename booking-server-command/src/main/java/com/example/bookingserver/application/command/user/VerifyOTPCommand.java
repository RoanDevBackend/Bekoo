package com.example.bookingserver.application.command.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyOTPCommand {
    @NotBlank(message = "Email không được để trống")
    String email;
    @NotNull
    Integer code;
}
