package com.example.bookingserver.application.query.handler.schedule;

import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.application.query.QueryBase;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FindAllScheduleHandler {
    ScheduleRepository scheduleRepository;
    ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public PageResponse<ScheduleResponse> execute(QueryBase<ScheduleResponse> queryBase) {
        Page<Schedule> page= scheduleRepository.findAll(queryBase.getPageable());
        List<ScheduleResponse> responses= page.getContent().stream().map(scheduleMapper::toResponse).toList();
        return PageResponse.<ScheduleResponse>builder()
                .pageIndex(queryBase.getPageIndex() )
                .pageSize(queryBase.getPageSize())
                .totalElements(page.getTotalElements())
                .contentResponse(responses)
                .totalPage(page.getTotalPages())
                .build();
    }
}
