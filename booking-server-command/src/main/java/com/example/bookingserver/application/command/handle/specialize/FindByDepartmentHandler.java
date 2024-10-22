package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class FindByDepartmentHandler {
    SpecializeRepository specializeRepository;
    SpecializeMapper specializeMapper;

    public PageResponse<SpecializeResponse> execute(String id, QueryBase<SpecializeResponse> queryBase){
        Pageable pageable= queryBase.getPageable();
        Page<Specialize> page= specializeRepository.findByDepartment(id, pageable);
        List<SpecializeResponse> content= new ArrayList<>();
        for(Specialize specialize: page.getContent()){
            var response= specializeMapper.toResponse(specialize);
            content.add(response);
        }
        return PageResponse.<SpecializeResponse>builder()
                .totalPage(page.getTotalPages())
                .contentResponse(content)
                .pageIndex(queryBase.getPageIndex())
                .orders(queryBase.getOrders())
                .pageSize(queryBase.getPageSize())
                .build();
    }
}
