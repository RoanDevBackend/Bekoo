package com.example.bookingserver.application.query.handler.schedule;

import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.mapper.ScheduleMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public PageResponse<FindByDoctorResponse> execute(String doctorId, Pageable pageable, LocalDateTime start, LocalDateTime end, int statusId){
        Page<Schedule> page= scheduleRepository.findByDoctor(doctorId, statusId, pageable, start, end);
        List<FindByDoctorResponse> contents= new ArrayList<>();
        for(Schedule schedule : page.getContent()){
            contents.add(scheduleMapper.toFindByDoctorResponse(schedule));
        }

        List<QueryBase.OrderDTO> orderDTOS= new ArrayList<>();
        orderDTOS.add(new QueryBase.OrderDTO("checkIn", Sort.Direction.ASC));

        return PageResponse.<FindByDoctorResponse>builder()
                .totalPage(page.getTotalPages())
                .orders(orderDTOS)
                .contentResponse(contents)
                .pageIndex(page.getNumber()+1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .build();
    }
}
