package com.example.bookingserver.application.command.reponse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TotalReportResponse {
    long totalDoctor;
    long totalPatient;
    long totalSchedule;
    long totalDepartment;
    long totalSpecialize;
}
