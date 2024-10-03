package com.example.bookingserver.application.command.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserCommand {
    @NotBlank
    String name;
    @NotBlank
    String phoneNumber;
    @NotBlank
    String email;
    @NotBlank
    String cccd;
    @NotBlank
    String province;
    @NotBlank
    String district;
    @NotBlank
    String commune;

    String aboutAddress;
    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;
    @NotBlank
    String dob;
    @NotBlank
    String gender;
}
