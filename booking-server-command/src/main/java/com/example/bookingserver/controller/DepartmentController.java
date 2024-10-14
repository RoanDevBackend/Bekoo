package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.department.DeleteDepartmentCommand;
import com.example.bookingserver.application.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.command.doctor_department.AddNewOneCommand;
import com.example.bookingserver.application.command.doctor_department.DeleteDoctorDepartmentCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.Handler_DTO;
import com.example.bookingserver.application.reponse.ApiResponse;
import com.example.bookingserver.application.reponse.DepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/department")
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    final Handler_DTO<CreateDepartmentCommand, DepartmentResponse> createDepartmentCommandDepartmentResponseHandler;
    final Handler_DTO<UpdateInfoDepartmentCommand, DepartmentResponse> updateInfoDepartmentCommandDepartmentResponseHandler;
    final Handler<DeleteDepartmentCommand> deleteDepartmentCommandHandler;
    final Handler<AddNewOneCommand> addNewOneCommandHandler;
    final Handler<DeleteDoctorDepartmentCommand> deleteDoctorDepartmentCommandHandler;

    @PostMapping
    public ApiResponse create(@RequestBody @Valid CreateDepartmentCommand command){
        var response= createDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(201, "Tạo chuyên khoa thành công", response, HttpStatus.CREATED);
    }

    @PostMapping("/doctor")
    public ApiResponse addNewOne(@RequestBody AddNewOneCommand addNewOneCommand){
        addNewOneCommandHandler.execute(addNewOneCommand);
        return ApiResponse.success(200, "Thêm thành công");
    }

    @PutMapping
    public ApiResponse update(@RequestBody @Valid UpdateInfoDepartmentCommand command){
        var response= updateInfoDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(200, "Cập nhập thành công", response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ApiResponse delete(@RequestBody DeleteDepartmentCommand command){
        deleteDepartmentCommandHandler.execute(command);
        return ApiResponse.success(200, "Xoá thành công");
    }

    @DeleteMapping("/doctor")
    public ApiResponse delete(@RequestBody DeleteDoctorDepartmentCommand command){
        deleteDoctorDepartmentCommandHandler.execute(command);
        return ApiResponse.success(200, "Xoá thành công");
    }



}
