package com.example.bookingserver.application.command.command.report;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportTemplate {
    LocalDate fromDate;
    LocalDate toDate;
    String groupType;
}
