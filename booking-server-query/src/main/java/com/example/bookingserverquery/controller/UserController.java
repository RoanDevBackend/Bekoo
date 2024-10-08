package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.FindByIdHandler;
import com.example.bookingserverquery.application.handler.FindByNameHandler;
import com.example.bookingserverquery.application.handler.GetAllHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.query.user.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.application.reponse.user.GetAllResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final FindByNameHandler findByNameHandler;
    final GetAllHandler getAllHandler;
    final FindByIdHandler findByIdHandler;

    @GetMapping("/name")
    public ApiResponse findByName(@RequestBody @Valid FindByNameQuery query){
        FindByNameResponse response= findByNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping
    public ApiResponse getAll(@RequestBody(required = false)@Valid QueryBase query){
        if(query == null){
            query= QueryBase.builder().build();
        }
        GetAllResponse response= getAllHandler.getAll(query);
        return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
    }

    @GetMapping("/id/{id}")
    public ApiResponse findById(@PathVariable String id){
        var response= findByIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm người dùng", response);
    }
}
