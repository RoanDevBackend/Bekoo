package com.example.bookingserver.application.query.handler.schedule;

import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.mapper.ScheduleMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FindHistoryScheduleByDoctorHandler {

    ScheduleRepository scheduleRepository;
    ScheduleMapper scheduleMapper;

    public PageResponse<FindByDoctorResponse> execute(String doctorId, QueryBase<FindByDoctorResponse> queryBase, LocalDateTime start, LocalDateTime end){
        Page<Schedule> page= scheduleRepository.findByDoctor(doctorId, queryBase.getPageable(), start, end);
        List<FindByDoctorResponse> contents= new ArrayList<>();
        for(Schedule schedule : page.getContent()){
            contents.add(scheduleMapper.toFindByDoctorResponse(schedule));
        }
        return PageResponse.<FindByDoctorResponse>builder()
                .totalPage(page.getTotalPages())
                .orders(queryBase.getOrders())
                .contentResponse(contents)
                .pageIndex(queryBase.getPageIndex())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .build();
    }
}
