package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.doctor.FindByDoctorIdHandler;
import com.example.bookingserverquery.application.handler.doctor.FindByDoctorNameHandler;
import com.example.bookingserverquery.application.handler.doctor.GetAllDoctorHandler;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/doctor")
@RequiredArgsConstructor
@RestController
public class DoctorController {

    final FindByDoctorNameHandler findByDoctorNameHandler;
    final GetAllDoctorHandler getAllDoctorHandler;
    final FindByDoctorIdHandler findByDoctorIdHandler;

    @GetMapping
    public ApiResponse findByName(@RequestParam(required = false) String name,
                                  @RequestParam(required = false, defaultValue = "1") int pageIndex ,
                                  @RequestParam(required = false, defaultValue = "10000") int pageSize){
        if(name == null){
            var query= QueryBase.<DoctorResponse>builder()
                    .pageIndex(pageIndex)
                    .pageSize(pageSize)
                    .build();
            PageResponse<DoctorResponse> response= getAllDoctorHandler.getAll(query);
            return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
        }
        var query= FindByNameQuery.<DoctorResponse>builder()
                .name(name)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findByDoctorNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm bác sĩ theo tên", response);
    }

    @GetMapping("/list")
    @Operation(deprecated = true)
    public ApiResponse getAll(@RequestParam(required = false, defaultValue = "1") int pageIndex ,
                              @RequestParam(required = false, defaultValue = "10000") int pageSize){
        var query= QueryBase.<DoctorResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        PageResponse<DoctorResponse> response= getAllDoctorHandler.getAll(query);
        return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
    }

    @GetMapping("/id/{id}")
    public ApiResponse findById(@PathVariable String id){
        var response= findByDoctorIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm người dùng", response);
    }
}
