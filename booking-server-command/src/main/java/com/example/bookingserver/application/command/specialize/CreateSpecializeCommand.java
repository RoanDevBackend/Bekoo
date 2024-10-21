package com.example.bookingserver.application.command.specialize;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CreateSpecializeCommand {
    String name;
    String description;
    String departmentId;
}
