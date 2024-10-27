package com.example.bookingserverquery.application.event.models.department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoDepartmentEvent {
    String id;
    String name;
    String description;
    String urlImage;
}
