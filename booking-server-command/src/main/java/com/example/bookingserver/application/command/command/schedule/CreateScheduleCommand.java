package com.example.bookingserver.application.command.command.schedule;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateScheduleCommand {
    @NotBlank(message = "Không được bỏ trống thông tin bệnh nhân")
    String patientId;
    @NotBlank(message = "Mã bác sĩ không được bỏ trống")
    String doctorId;
    @NotBlank(message = "Mã gói khám không được bỏ trống")
    String specializeId;
    @NotBlank(message = "Không bỏ trống thời gian tới khám")
    String checkIn;
    String note;
    /**
     * 1 Thanh toán khi tới khám
     * 2 Thanh toán online
     */
    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    Integer paymentMethod;
}
