package com.example.bookingserver.application.command.handle.report;

import com.example.bookingserver.application.command.reponse.ReportResponse;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.persistence.repository.ScheduleJpaRepository;
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

    public List<ReportResponse> executeByDoctor(String doctorId, LocalDate from, LocalDate to, int groupType) {

        List<ReportResponse> responses= new ArrayList<>();
        while (from.isBefore(to)){
            LocalDateTime start= from.atStartOfDay();
            LocalDateTime end= this.nextTo(from, groupType).atStartOfDay();
            int value= scheduleJpaRepository.getCountByDoctor(doctorId, start, end);
            responses.add(new ReportResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
    }

    public List<ReportResponse> executeTotal(LocalDate from, LocalDate to, int groupType) {
        List<ReportResponse> responses= new ArrayList<>();
        while (from.isBefore(to)){
            LocalDateTime start= from.atStartOfDay();
            LocalDateTime end= this.nextTo(from, groupType).atStartOfDay();
            int value= scheduleJpaRepository.getTotalValue(start, end);
            responses.add(new ReportResponse(from, value));
            from = nextTo(from, groupType);
        }
        return responses;
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
