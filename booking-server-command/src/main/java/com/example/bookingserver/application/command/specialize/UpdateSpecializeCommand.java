package com.example.bookingserver.application.command.specialize;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSpecializeCommand {
    String id;
    String name;
    String description;
}
