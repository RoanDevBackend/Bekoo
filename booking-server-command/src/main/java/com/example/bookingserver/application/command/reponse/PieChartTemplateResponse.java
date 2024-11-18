package com.example.bookingserver.application.command.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PieChartTemplateResponse {
    String range;
    int value;
}
