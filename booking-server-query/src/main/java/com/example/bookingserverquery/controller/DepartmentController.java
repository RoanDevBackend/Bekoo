package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.department.FindByDepartmentIdHandler;
import com.example.bookingserverquery.application.handler.department.FindByDepartmentNameHandler;
import com.example.bookingserverquery.application.handler.department.FindDoctorByDepartmentHandler;
import com.example.bookingserverquery.application.handler.department.GetAllDepartmentHandler;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import document.response.DepartmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/department")
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class DepartmentController {
    
    private FindByDepartmentIdHandler findByDepartmentIdHandler;
    private FindByDepartmentNameHandler findByDepartmentNameHandler;
    private GetAllDepartmentHandler getAllDepartmentHandler;
    private FindDoctorByDepartmentHandler findDoctorByDepartmentHandler;

    @GetMapping("/id/{id}")
    @Operation(summary = "Tìm kiếm Chuyên khoa theo ID")
    public ApiResponse getDepartmentById(@PathVariable String id) {
        var response= findByDepartmentIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm kiếm theo tên")
    public ApiResponse getDepartmentByName(@PathVariable String name,
                                           @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                           @RequestParam(required = false, defaultValue = "10000") int pageSize) {
        var query = FindByNameQuery.<DepartmentResponse>builder()
                .name(name)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findByDepartmentNameHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping("/departments")
    @Operation(summary = "Lấy ra danh sách tất cả chuyên khoa")
    public ApiResponse getAllDepartments(@RequestParam(required = false, defaultValue = "1") int pageIndex,
                                         @RequestParam(required = false, defaultValue = "10000") int pageSize) {
        var query= QueryBase.<DepartmentResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= getAllDepartmentHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Lấy danh sách bác sĩ của phòng ban bệnh viện")
    @GetMapping("doctor/{id}")
    public ApiResponse findDoctorByDepartment(@PathVariable String id,
                                              @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                              @RequestParam(required = false, defaultValue = "10000") int pageSize){
        var query= QueryBase.<DoctorResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= findDoctorByDepartmentHandler.execute(id, query);
        return ApiResponse.success(200, "Lấy danh sách bác sĩ theo phòng ban", response);
    }
}