package com.example.bookingserverquery.application.handler;

import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.user.GetAllResponse;
import com.example.bookingserverquery.application.reponse.user.UserResponse;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.UserRepository;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllHandler {

    final UserRepository userRepository;
    final UserMapper userMapper;


    public GetAllResponse getAll(QueryBase query){
        Page<User> page= userRepository.getAll(query.getPageable());
        List<UserResponse> userResponses= new ArrayList<>();
        for(User x: page.getContent()){
            UserResponse userResponse= userMapper.toResponse(x);
            userResponses.add(userResponse);
        }
        return GetAllResponse.builder()
                .userResponses(userResponses)
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .orders(query.getOrders())
                .totalPage(page.getTotalPages())
                .build();
    }

}
