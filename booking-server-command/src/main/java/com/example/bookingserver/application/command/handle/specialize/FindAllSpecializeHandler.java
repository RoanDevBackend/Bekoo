package com.example.bookingserver.application.command.handle.specialize;


import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.query.handler.response.PageResponse;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FindAllSpecializeHandler {
    SpecializeRepository specializeRepository;
    SpecializeMapper specializeMapper;

    public PageResponse<SpecializeResponse> execute(QueryBase<SpecializeResponse> queryBase){
        Page<Specialize> page= specializeRepository.findAll(queryBase.getPageable());
        List<SpecializeResponse> contents= new ArrayList<>();
        for(Specialize specialize: page.getContent()){
            var response= specializeMapper.toResponse(specialize);
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
