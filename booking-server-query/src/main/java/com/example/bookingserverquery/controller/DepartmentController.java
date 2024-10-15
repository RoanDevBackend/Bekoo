package com.example.bookingserverquery.controller;


import com.example.bookingserverquery.application.handler.department.FindByDepartmentIdHandler;
import com.example.bookingserverquery.application.handler.department.FindByDepartmentNameHandler;
import com.example.bookingserverquery.application.handler.department.GetAllDepartmentHandler;
import com.example.bookingserverquery.application.query.FindByNameQuery;
import com.example.bookingserverquery.application.query.QueryBase;
import com.example.bookingserverquery.application.reponse.ApiResponse;
import com.example.bookingserverquery.application.reponse.department.DepartmentResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private FindByDepartmentIdHandler findByDepartmentIdHandler;

    @Autowired
    private FindByDepartmentNameHandler findByDepartmentNameHandler;

    @Autowired
    private GetAllDepartmentHandler getAllDepartmentHandler;

    @GetMapping("/id/{id}")
    public ApiResponse getDepartmentById(@PathVariable String id) {
        var response= findByDepartmentIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping("/name")
    public ApiResponse getDepartmentByName(@RequestBody @Valid FindByNameQuery<DepartmentResponse> query) {

        var response= findByDepartmentNameHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @GetMapping
    public ApiResponse getAllDepartments(@RequestBody(required = false) @Valid QueryBase<DepartmentResponse> query) {
        if(query == null){
            query= FindByNameQuery.<DepartmentResponse>builder().build();
        }
        var response= getAllDepartmentHandler.execute(query);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }
}