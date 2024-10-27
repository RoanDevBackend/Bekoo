package com.example.bookingserverquery.application.reponse.department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    String id;
    String name;
    String description;
    String urlImage;
    Integer totalSpecialize;
    Integer totalDoctor;
}
