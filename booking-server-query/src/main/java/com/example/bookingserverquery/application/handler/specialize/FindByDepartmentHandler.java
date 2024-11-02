package com.example.bookingserverquery.application.handler.specialize;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserverquery.infrastructure.repository.ScheduleELRepository;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.SpecializeResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class FindByDepartmentHandler {
    SpecializeELRepository specializeRepository;
    SpecializeMapper specializeMapper;
    ScheduleELRepository scheduleELRepository;

    public PageResponse<SpecializeResponse> execute(String id, QueryBase<SpecializeResponse> queryBase){
        Page<Specialize> page= specializeRepository.findAllByDepartment(id, queryBase.getPageable());
        List<SpecializeResponse> contents= new ArrayList<>();
        for(Specialize specialize: page.getContent()){
            var response= specializeMapper.toResponse(specialize);
            Long totalPatient= scheduleELRepository.countBySpecializedId(specialize.getId());
            response.setTotalPatient(totalPatient);
            contents.add(response);
        }
        return PageResponse.<SpecializeResponse>builder()
                .totalPage(page.getTotalPages())
                .contentResponse(contents)
                .pageIndex(queryBase.getPageIndex())
                .orders(queryBase.getOrders())
                .pageSize(queryBase.getPageSize())
                .totalElements(page.getTotalElements())
                .build();
    }
}
