package com.example.bookingserver.application.command.command.patient;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateInfoPatientCommand {
    @NotBlank(message = "Không được bỏ trống mã người dùng")
    String userId;
    @NotBlank(message = "Không được bỏ trống số thẻ bảo hiểm y tế")
    String healthInsuranceNumber;
    String bloodType= "N/A";
    @NotNull(message = "Không được bỏ trống thông tin liên hệ khẩn cấp")
            @Valid
    EmergencyContactCommand emergencyContactCommand;
}
