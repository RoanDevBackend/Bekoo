package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.specialize.FindByDepartmentHandler;
import com.example.bookingserverquery.application.handler.specialize.GetAllSpecializeHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import document.response.ApiResponse;
import document.response.SpecializeResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/specialize")
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class SpecializeController {

    GetAllSpecializeHandler getAllSpecializeHandler;
    FindByDepartmentHandler findByDepartmentHandler;

    @Operation(summary = "Lấy ra tất cả")
    @GetMapping
    public ApiResponse findAll(@RequestParam(required = false, defaultValue = "1") int pageIndex ,
                               @RequestParam(required = false, defaultValue = "10000") int pageSize){
        QueryBase<SpecializeResponse> queryBase= QueryBase.<SpecializeResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= getAllSpecializeHandler.execute(queryBase);
        return ApiResponse.success(200, "Tìm kiếm tất cả chuyên ngành đang có", response);
    }

    @Operation(summary = "Tìm kiếm chuyên ngành của chuyên khoa")
    @GetMapping(value = "/department/{id}")
    public ApiResponse getByDepartment(@PathVariable("id") String id,
                                       @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                       @RequestParam(required = false, defaultValue = "10000") int pageSize){
        var queryBase = QueryBase.<SpecializeResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findByDepartmentHandler.execute(id, queryBase);
        return ApiResponse.success(200, "Tìm kiếm theo chuyên khoa thành công" ,response);
    }

}
