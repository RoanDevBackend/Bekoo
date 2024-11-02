package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.doctor.FindByDoctorIdHandler;
import com.example.bookingserverquery.application.handler.doctor.FindByDoctorNameHandler;
import com.example.bookingserverquery.application.handler.doctor.GetAllDoctorHandler;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.PageResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/doctor")
@RequiredArgsConstructor
@RestController
public class DoctorController {

    final FindByDoctorNameHandler findByDoctorNameHandler;
    final GetAllDoctorHandler getAllDoctorHandler;
    final FindByDoctorIdHandler findByDoctorIdHandler;

    @GetMapping("/name/{name}")
    public ApiResponse findByName(@PathVariable String name,
                                  @RequestParam(required = false, defaultValue = "1") int pageIndex ,
                                  @RequestParam(required = false, defaultValue = "10000") int pageSize){
        var query= FindByNameQuery.<DoctorResponse>builder()
                .name(name)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findByDoctorNameHandler.findByName(query);
        return ApiResponse.success(200, "Tìm kiếm bác sĩ theo tên", response);
    }

    @GetMapping
    public ApiResponse getAll(@RequestParam(required = false, defaultValue = "1") int pageIndex ,
                              @RequestParam(required = false, defaultValue = "10000") int pageSize){
        var query= QueryBase.<DoctorResponse>builder().build();
        PageResponse<DoctorResponse> response= getAllDoctorHandler.getAll(query);
        return ApiResponse.success(200, "Tìm kiếm tất cả giá trị", response);
    }

    @GetMapping("/id/{id}")
    public ApiResponse findById(@PathVariable String id){
        var response= findByDoctorIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm người dùng", response);
    }
}
