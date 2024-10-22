package com.example.bookingserver.application.command.command.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordCommand {
    @NotBlank(message = "Email không được bỏ trống")
    String email;
}
