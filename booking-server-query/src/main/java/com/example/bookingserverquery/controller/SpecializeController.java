package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.specialize.FindByDepartmentHandler;
import com.example.bookingserverquery.application.handler.specialize.FindSpecializeByIdHandler;
import com.example.bookingserverquery.application.handler.specialize.GetAllSpecializeHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import document.response.ApiResponse;
import document.response.SpecializeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    FindSpecializeByIdHandler findSpecializeByIdHandler;

    @Operation(summary = "Lấy ra tất cả")
    @GetMapping
    public ApiResponse findAll(@RequestParam(required = false, defaultValue = "1") int pageIndex ,
                               @RequestParam(required = false, defaultValue = "10000") int pageSize){
        QueryBase<SpecializeResponse> queryBase= QueryBase.<SpecializeResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= getAllSpecializeHandler.execute(queryBase);
        return ApiResponse.success(200, "Tìm kiếm tất cả gói khám", response);
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
        return ApiResponse.success(200, "Tìm kiếm gói khám theo chuyên khoa" ,response);
    }

    @Operation(summary = "Tìm kiếm gói khám theo mã gói khám", parameters = {
            @Parameter(name = "id", description = "Mã gói khám")
    })
    @GetMapping("/{id}")
    public ApiResponse findById(@PathVariable String id){
        var response = findSpecializeByIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm gói khám bằng mã gói khám", response);
    }
}
