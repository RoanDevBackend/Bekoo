package com.example.bookingserver.application.command.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordOTPCommand {
    String email;
    String newPassword;
    String confirmPassword;
}
