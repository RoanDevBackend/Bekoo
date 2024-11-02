package com.example.bookingserver.application.command.command.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDepartmentCommand {
    @NotBlank(message = "Không được bỏ trống tên cho khoa")
    String name;
    @NotBlank(message = "Không được bỏ trống mô tả cho khoa")
    String description;
    @NotNull(message = "Không được bỏ trống ảnh")
    MultipartFile image;
}
