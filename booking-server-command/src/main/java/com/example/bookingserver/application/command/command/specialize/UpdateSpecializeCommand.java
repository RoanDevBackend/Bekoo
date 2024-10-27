package com.example.bookingserver.application.command.command.specialize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSpecializeCommand {
    String id;
    @NotBlank(message = "Không được thiếu tên dịch vụ này")
    String name;
    String description;
    @Min(value = 1, message = "Giá tiền dịch vụ này không nhỏ hơn 1")
            @NotNull(message = "Giá tiền không được bỏ trống")
    Integer price;
}
