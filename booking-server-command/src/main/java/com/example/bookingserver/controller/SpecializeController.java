package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.command.handle.specialize.*;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/specialize")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecializeController {

    CreateSpecializeHandler createSpecializeHandler;
    UpdateSpecializeHandler updateSpecializeHandler;
    DeleteSpecializeHandler deleteSpecializeHandler;
    FindBySpecializeIdHandler findBySpecializeIdHandler;
    FindByDepartmentHandler findByDepartmentHandler;
    FindAllSpecializeHandler findAllSpecializeHandler;

    @PostMapping
    @Operation(summary = "Thêm chuyên khoa")
    public ApiResponse create(@RequestBody @Valid CreateSpecializeCommand command){
        var response= createSpecializeHandler.execute(command);
        return ApiResponse.success(201, "Thêm thành công", response, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Sửa chuyên khoa")
    public ApiResponse update(@RequestBody @Valid UpdateSpecializeCommand command){
        var response= updateSpecializeHandler.execute(command);
        return ApiResponse.success(200, "Sửa thành công", response);
    }


    @DeleteMapping(value = "/{ids}")
    @Operation(summary = "Xoá chuyên khoa")
    public ApiResponse delete(@PathVariable("ids") List<String> ids){
        deleteSpecializeHandler.execute(ids);
        return ApiResponse.success(200, "Xoá thành công");
    }

    @Operation(summary = "Tìm kiếm theo ID")
    @GetMapping(value = "/{id}")
    public ApiResponse get(@PathVariable("id") String id){
        var response= findBySpecializeIdHandler.execute(id);
        return ApiResponse.success(200, "Tìm kiếm  theo mã định danh thành công", response);
    }
    @Operation(summary = "Lấy ra tất cả")
    @PostMapping(value = "/query-all")
    public ApiResponse findAll(@RequestBody(required = false) QueryBase<SpecializeResponse> queryBase){
        if(queryBase== null){
            queryBase= QueryBase.<SpecializeResponse>builder().build();
        }
        var response= findAllSpecializeHandler.execute(queryBase);
        return ApiResponse.success(200, "Tìm kiếm tất cả chuyên ngành đang có", response);
    }
    @Operation(summary = "Tìm kiếm chuyên ngành của chuyên khoa")
    @PostMapping(value = "/department/{id}")
    public ApiResponse getByDepartment(@PathVariable("id") String id, @RequestBody(required = false) QueryBase<SpecializeResponse> queryBase){
        if(queryBase == null) queryBase = QueryBase.<SpecializeResponse>builder().build();
        var response= findByDepartmentHandler.execute(id, queryBase);
        return ApiResponse.success(200, "Tìm kiếm theo chuyên khoa thành công" ,response);
    }
}
