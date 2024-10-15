package com.example.bookingserverquery.application.handler.user;

import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.FindByNameResponse;
import com.example.bookingserverquery.application.reponse.user.UserResponse;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.UserRepository;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByNameHandler {

    final UserRepository userRepository;
    final UserMapper userMapper;

    public FindByNameResponse<UserResponse> findByName(FindByNameQuery<UserResponse> query){
        Pageable pageable= query.getPageable();

        Page<User> page= userRepository.findByName(query.getName(), pageable);

        List<UserResponse> userResponses= new ArrayList<>();
        for(User x: page.getContent()){
            UserResponse userResponse= userMapper.toResponse(x);
            userResponses.add(userResponse);
        }

        return FindByNameResponse.<UserResponse>builder()
                .name(query.getName())
                .totalPage(page.getTotalPages())
                .pageSize(page.getSize())
                .pageIndex(page.getNumber() + 1)
                .orders(query.getOrders())
                .contentResponse(userResponses)
                .build();
    }
}
