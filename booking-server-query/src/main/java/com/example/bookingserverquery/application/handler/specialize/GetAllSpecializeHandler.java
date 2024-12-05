package com.example.bookingserverquery.application.handler.specialize;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.SpecializeResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class GetAllSpecializeHandler {
    SpecializeELRepository specializeRepository;
    SpecializeUtils specializeUtils;

    public PageResponse<SpecializeResponse> execute(QueryBase<SpecializeResponse> queryBase){
        Page<Specialize> page= specializeRepository.findAll(queryBase.getPageable());
        return PageResponse.<SpecializeResponse>builder()
                .totalPage(page.getTotalPages())
                .contentResponse(specializeUtils.convertToResponseList(page.getContent()))
                .pageIndex(queryBase.getPageIndex())
                .orders(queryBase.getOrders())
                .pageSize(queryBase.getPageSize())
                .totalElements(page.getTotalElements())
                .build();
    }
}
