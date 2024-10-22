package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.command.command.doctor_department.AddNewOneCommand;
import com.example.bookingserver.application.command.command.doctor_department.DeleteDoctorDepartmentCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.Handler_DTO;
import com.example.bookingserver.application.command.handle.department.DeleteDepartmentHandler;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.DepartmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/department")
@RestController
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class DepartmentController {

    Handler_DTO<CreateDepartmentCommand, DepartmentResponse> createDepartmentCommandDepartmentResponseHandler;
    Handler_DTO<UpdateInfoDepartmentCommand, DepartmentResponse> updateInfoDepartmentCommandDepartmentResponseHandler;
    DeleteDepartmentHandler deleteDepartmentHandler;
    Handler<AddNewOneCommand> addNewOneCommandHandler;
    Handler<DeleteDoctorDepartmentCommand> deleteDoctorDepartmentCommandHandler;

    @PostMapping
    public ApiResponse create(@RequestBody @Valid CreateDepartmentCommand command){
        var response= createDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(201, "Tạo chuyên khoa thành công", response, HttpStatus.CREATED);
    }

    @PostMapping("/doctor/new-one")
    public ApiResponse addNewOne(@RequestBody AddNewOneCommand addNewOneCommand){
        addNewOneCommandHandler.execute(addNewOneCommand);
        return ApiResponse.success(200, "Thêm thành công");
    }

    @PutMapping
    public ApiResponse update(@RequestBody @Valid UpdateInfoDepartmentCommand command){
        var response= updateInfoDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(200, "Cập nhập thành công", response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{ids}")
    public ApiResponse delete(@PathVariable List<String> ids){
        deleteDepartmentHandler.execute(ids);
        return ApiResponse.success(200, "Xoá thành công");
    }

    @PostMapping("/doctor/delete")
    public ApiResponse delete(@RequestBody DeleteDoctorDepartmentCommand command){
        deleteDoctorDepartmentCommandHandler.execute(command);
        return ApiResponse.success(200, "Xoá thành công");
    }



}
