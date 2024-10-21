package com.example.bookingserver.application.reponse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecializeResponse {
    String id;
    String name;
    String description;
    DepartmentResponse department;
}
