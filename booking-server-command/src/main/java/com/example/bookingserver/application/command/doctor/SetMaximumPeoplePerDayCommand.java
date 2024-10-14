package com.example.bookingserver.application.command.doctor;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetMaximumPeoplePerDayCommand {
    String id;
    Integer value;
}
