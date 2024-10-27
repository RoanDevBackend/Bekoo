package com.example.bookingserver.application.command.event.specialize;

import com.example.bookingserver.domain.Department;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecializeEvent {
    String id;
    String name;
    String description;
    Integer price;
    Department department;
}
