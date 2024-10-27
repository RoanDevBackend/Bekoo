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
    @NotBlank(message = "Không được bỏ trống thông tin bệnh nhân, ex: patientId")
    String patientId;
    @NotBlank(message = "Mã bác sĩ không được bỏ trống")
    String doctorId;
    @NotBlank(message = "Mã gói khám không được bỏ trống")
    String specializeId;
    @NotBlank(message = "Không bỏ trống thời gian tới khám")
    String checkIn;
    String note;
}
