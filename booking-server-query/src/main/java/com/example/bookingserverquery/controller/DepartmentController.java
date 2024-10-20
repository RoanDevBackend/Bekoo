package com.example.bookingserverquery.controller;


import com.example.bookingserverquery.application.handler.department.FindByDepartmentIdHandler;
import com.example.bookingserverquery.application.handler.department.FindByDepartmentNameHandler;
import com.example.bookingserverquery.application.handler.department.FindDoctorByDepartmentHandler;
import com.example.bookingserverquery.application.handler.department.GetAllDepartmentHandler;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.department.DepartmentResponse;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


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
    public ApiResponse getDepartmentById(@PathVariable String id) {
        var response= findByDepartmentIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @PostMapping("/name")
    public ApiResponse getDepartmentByName(@RequestBody @Valid FindByNameQuery<DepartmentResponse> query) {

        var response= findByDepartmentNameHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @PostMapping
    public ApiResponse getAllDepartments(@RequestBody(required = false) @Valid QueryBase<DepartmentResponse> query) {
        if(query == null){
            query= FindByNameQuery.<DepartmentResponse>builder().build();
        }
        var response= getAllDepartmentHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Lấy danh sách bác sĩ của phòng ban bệnh viện")
    @PostMapping("doctor/{id}")
    public ApiResponse findDoctorByDepartment(@PathVariable String id, @RequestBody(required = false) @Valid QueryBase<DoctorResponse> query){
        if(query == null){
            query= QueryBase.<DoctorResponse>builder().build();
        }
        var response= findDoctorByDepartmentHandler.execute(id, query);
        return ApiResponse.success(200, "Lấy danh sách bác sĩ theo phòng ban", response);
    }
}