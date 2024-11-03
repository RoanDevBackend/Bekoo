package com.example.bookingserver.application.command.handle.report;

import com.example.bookingserver.application.command.reponse.ReportChartTemplateResponse;
import com.example.bookingserver.application.command.reponse.TotalReportResponse;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportByHandler {

    final ScheduleJpaRepository scheduleJpaRepository;
    final DoctorJpaRepository doctorJpaRepository;
    final PatientRepository patientJpaRepository;
    final SpecializeJpaRepository specializeJpaRepository;
    final DepartmentJpaRepository departmentJpaRepository;

    public List<ReportChartTemplateResponse> executeByDoctor(String doctorId, LocalDate from, LocalDate to, int groupType) {
        List<ReportChartTemplateResponse> responses= new ArrayList<>();
        from = this.nextTo(from, groupType);
        to = this.nextTo(to, groupType);
        while (from.isBefore(to)){
            LocalDateTime start= from.atStartOfDay();
            LocalDateTime end= this.nextTo(from, groupType).atStartOfDay();
            int value= scheduleJpaRepository.getCountByDoctorAndCreatedAt(doctorId, start, end);
            responses.add(new ReportChartTemplateResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
    }

    public List<ReportChartTemplateResponse> executeTotal(LocalDate from, LocalDate to, int groupType) {
        List<ReportChartTemplateResponse> responses= new ArrayList<>();
        from = this.nextTo(from, groupType);
        to = this.nextTo(to, groupType);
        while (from.isBefore(to)){
            LocalDateTime start= from.atStartOfDay();
            LocalDateTime end= this.nextTo(from, groupType).atStartOfDay();
            int value= scheduleJpaRepository.getTotalValue(start, end);
            responses.add(new ReportChartTemplateResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
    }

    public TotalReportResponse execute(){
        long totalDoctor= doctorJpaRepository.count();
        long totalPatient= patientJpaRepository.count();
        long totalSpecialize= specializeJpaRepository.count();
        long totalDepartment= departmentJpaRepository.count();
        long totalSchedule= scheduleJpaRepository.count();
        return TotalReportResponse.builder()
                .totalDoctor(totalDoctor)
                .totalPatient(totalPatient)
                .totalSpecialize(totalSpecialize)
                .totalDepartment(totalDepartment)
                .totalSchedule(totalSchedule)
                .build();
    }

    private LocalDate nextTo(LocalDate from, int groupType){
        if(groupType == (ApplicationConstant.DateType.DAY))
            return from.plusDays(1);
        else if(groupType == (ApplicationConstant.DateType.WEEK)){
            return from.plusWeeks(1);
        }else if(groupType == (ApplicationConstant.DateType.MONTH)){
            return from.plusMonths(1);
        }else{
            return from.plusYears(1);
        }
    }


}
