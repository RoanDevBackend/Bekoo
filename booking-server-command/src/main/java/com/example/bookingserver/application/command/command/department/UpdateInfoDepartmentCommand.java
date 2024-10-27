package com.example.bookingserver.application.command.command.department;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDepartmentCommand {
    String id;
    String name;
    String description;
    MultipartFile image;
}
