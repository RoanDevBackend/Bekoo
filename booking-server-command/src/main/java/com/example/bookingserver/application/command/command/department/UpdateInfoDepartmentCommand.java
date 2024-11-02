package com.example.bookingserver.application.command.command.department;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDepartmentCommand {
    @NotBlank(message = "Thiếu thông tin định danh")
    String id;
    @NotBlank(message = "Không được bỏ trống tên ")
    String name;
    @NotBlank(message = "Không được bỏ trống mô tả ")
    String description;
    MultipartFile image;
}
