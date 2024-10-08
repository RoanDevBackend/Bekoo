package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.FindByNameHandler;
import com.example.bookingserverquery.application.query.user.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final FindByNameHandler findByNameHandler;

    @GetMapping("/name")
    public ApiResponse findByName(@RequestBody FindByNameQuery query){
        FindByNameResponse response= findByNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }
}
