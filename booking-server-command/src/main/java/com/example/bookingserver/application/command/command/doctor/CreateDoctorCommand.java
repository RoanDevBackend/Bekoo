package com.example.bookingserver.application.command.command.doctor;

import com.example.bookingserver.application.command.command.user.CreateUserCommand;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDoctorCommand  {
    String trainingBy;
    String description;
    @NotNull(message = "Không bỏ trống người dùng")
    CreateUserCommand user;
}
