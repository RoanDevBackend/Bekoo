package com.example.bookingserverquery.application.event.models.specialize;

import com.example.bookingserverquery.domain.Department;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SpecializeEvent {
    String id;
    String name;
    String description;
    Integer price;
    Department department;
}
