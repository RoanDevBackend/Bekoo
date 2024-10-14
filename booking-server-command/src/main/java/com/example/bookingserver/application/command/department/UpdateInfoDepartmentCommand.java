package com.example.bookingserver.application.command.department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDepartmentCommand {
    String id;
    String name;
    String description;
}
