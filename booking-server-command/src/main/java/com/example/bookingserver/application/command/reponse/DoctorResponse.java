package com.example.bookingserver.application.command.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse{
    String id;
    String description;
    String trainingBy;
    @JsonProperty(value = "info")
    UserResponse user;
}
