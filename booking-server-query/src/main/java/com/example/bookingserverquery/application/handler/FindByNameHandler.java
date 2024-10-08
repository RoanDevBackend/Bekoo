package com.example.bookingserverquery.application.handler;

import com.example.bookingserverquery.application.query.user.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByNameHandler {

    final UserELRepository userELRepository;

    public FindByNameResponse findByName(FindByNameQuery query){
        Pageable pageable= query.getPageable();

        Page<User> page= userELRepository.findAll(pageable);




        return FindByNameResponse.builder()
                .name(query.getName())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(page.getNumber())
                .orders(query.getOrders())
                .content(page.getContent())
                .build();
    }
}
