package com.example.bookingserver.application.command.doctor_department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddNewOneCommand {
    String doctorId;
    String departmentId;
}
