package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.user.FindByIdHandler;
import com.example.bookingserverquery.application.handler.user.FindByNameHandler;
import com.example.bookingserverquery.application.handler.user.GetAllHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.FindByNameResponse;
import com.example.bookingserverquery.application.reponse.GetAllResponse;
import com.example.bookingserverquery.application.reponse.user.UserResponse;
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
    public ApiResponse findByName(@RequestBody @Valid FindByNameQuery<UserResponse> query){
        var response= findByNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping
    public ApiResponse getAll(@RequestBody(required = false)@Valid QueryBase<UserResponse> query){
        if(query == null){
            query= QueryBase.<UserResponse>builder().build();
        }
        GetAllResponse<UserResponse> response= getAllHandler.getAll(query);
        return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
    }

    @GetMapping("/id/{id}")
    public ApiResponse findById(@PathVariable String id){
        var response= findByIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm người dùng", response);
    }
}
