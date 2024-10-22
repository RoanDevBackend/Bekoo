package com.example.bookingserver.application.command.command.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordCommand {
    @NotBlank
    String id;
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
    @NotBlank
    String confirmPassword;
}
