package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.user.FindByIdHandler;
import com.example.bookingserverquery.application.handler.user.FindByNameHandler;
import com.example.bookingserverquery.application.handler.user.GetAllHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.PageResponse;
import document.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    public ApiResponse findByName(@RequestParam(required = false) String name,
                                  @RequestParam(required = false, defaultValue = "1") int pageIndex ,
                                  @RequestParam(required = false, defaultValue = "10000") int pageSize) {
        var query= FindByNameQuery.<UserResponse>builder()
                .name(name)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findByNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping
    public ApiResponse getAll(@RequestParam(required = false, defaultValue = "1") int pageIndex ,
                              @RequestParam(required = false, defaultValue = "10000") int pageSize){
         var query= QueryBase.<UserResponse>builder()
                 .pageIndex(pageIndex)
                 .pageSize(pageSize)
                 .build();
        PageResponse<UserResponse> response= getAllHandler.getAll(query);
        return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Tìm kiếm người dùng bằng mã người dùng")
    public ApiResponse findById(@PathVariable String id){
        var response= findByIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm người dùng", response);
    }
}
