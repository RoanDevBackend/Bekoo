package com.example.bookingserver.application.command.command.patient;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmergencyContactCommand {
    String name;
    @Pattern(regexp = "^0[3|5|7|8|9][0-9]{8}$", message = "Định dạng số điện thoại không hợp lệ")
    String phone;
    String address;
    String relationship;
}
