package com.example.bookingserver.application.command.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordCommand {
    @NotBlank
    String phoneNumber;
}
