package com.example.bookingserver.application.command.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportChartTemplateResponse {
    LocalDate time;
    int value;
}
