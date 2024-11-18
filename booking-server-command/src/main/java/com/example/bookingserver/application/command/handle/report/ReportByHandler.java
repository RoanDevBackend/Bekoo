package com.example.bookingserver.application.command.handle.report;

import com.example.bookingserver.application.command.reponse.PieChartTemplateResponse;
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
    final UserJpaRepository userJpaRepository;
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

    public List<PieChartTemplateResponse> getByAge(){
        int from1to15= userJpaRepository.countByDob(LocalDate.of(LocalDate.now().getYear()-15, 1, 1), LocalDate.of(LocalDate.now().getYear() + 1, 1, 1));
        int from16to30= userJpaRepository.countByDob(LocalDate.of(LocalDate.now().getYear()-30, 1, 1), LocalDate.of(LocalDate.now().getYear() - 16 + 1 , 1, 1));
        int from31to45= userJpaRepository.countByDob(LocalDate.of(LocalDate.now().getYear()-45, 1, 1), LocalDate.of(LocalDate.now().getYear() - 31  + 1, 1, 1));
        int from46to60= userJpaRepository.countByDob(LocalDate.of(LocalDate.now().getYear()-60, 1, 1), LocalDate.of(LocalDate.now().getYear() - 46  + 1 , 1, 1));
        int over60= userJpaRepository.countByDob(LocalDate.of(LocalDate.now().getYear()-150, 1, 1), LocalDate.of(LocalDate.now().getYear() - 61  + 1, 1, 1));
        List<PieChartTemplateResponse> responses= new ArrayList<>();
        responses.add(new PieChartTemplateResponse("1-15", from1to15));
        responses.add(new PieChartTemplateResponse("16-30", from16to30));
        responses.add(new PieChartTemplateResponse("31-45", from31to45));
        responses.add(new PieChartTemplateResponse("46-60", from46to60));
        responses.add(new PieChartTemplateResponse("60+", over60));
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
