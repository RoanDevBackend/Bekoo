package com.example.bookingserver.application.command.command.schedule;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateScheduleCommand {
    @NotBlank
    String userId;
    @NotBlank
    String doctorId;
    @NotBlank
    String specializeId;
    @NotBlank
    String checkIn;
    String note;
}
