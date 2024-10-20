package com.example.bookingserver.application.command.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoUserCommand {
    @NotBlank
    String id;
    @NotBlank
    String name;
    @NotBlank
    String province;
    @NotBlank
    String district;
    @NotBlank
    String commune;
    String aboutAddress;
}
