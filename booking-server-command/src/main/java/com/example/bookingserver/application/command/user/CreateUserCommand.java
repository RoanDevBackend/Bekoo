package com.example.bookingserver.application.command.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserCommand {
    @NotBlank(message = "Tên không được để trống")
    String name;
    @NotBlank(message = "Số điện thoại không được để trống")
    String phoneNumber;
    @NotBlank(message = "Email không được để trống")
    String email;
    @NotBlank(message = "CCCD không được để trống")
    String cccd;
    @NotBlank(message = "Tỉnh không được để trống")
    String province;
    @NotBlank(message = "Quận/huyện không được để trống")
    String district;
    @NotBlank(message = "Phường/xã không được để trống")
    String commune;
    String aboutAddress;
    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    String confirmPassword;
    @NotBlank(message = "Ngày sinh không được để trống")
    String dob;
    @NotBlank(message = "Giới tính không được để trống")
    String gender;
}
