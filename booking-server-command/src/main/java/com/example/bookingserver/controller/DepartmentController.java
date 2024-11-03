package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.command.command.doctor_department.AddNewOneCommand;
import com.example.bookingserver.application.command.command.doctor_department.DeleteDoctorDepartmentCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.department.DeleteDepartmentHandler;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.DepartmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping(value = "/department")
@RestController
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class DepartmentController {

    HandlerDTO<CreateDepartmentCommand, DepartmentResponse> createDepartmentCommandDepartmentResponseHandler;
    HandlerDTO<UpdateInfoDepartmentCommand, DepartmentResponse> updateInfoDepartmentCommandDepartmentResponseHandler;
    DeleteDepartmentHandler deleteDepartmentHandler;
    Handler<AddNewOneCommand> addNewOneCommandHandler;
    Handler<DeleteDoctorDepartmentCommand> deleteDoctorDepartmentCommandHandler;

    @PostMapping
    @Operation(summary = "Thêm chuyên khoa mới")
    public ApiResponse create(@ModelAttribute @Valid CreateDepartmentCommand command){
        log.info("API: Create department");
        var response= createDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(201, "Tạo chuyên khoa thành công", response, HttpStatus.CREATED);
    }

    @PostMapping("/doctor/new-one")
    @Operation(summary = "Thêm bác sĩ vào chuyên khoa")
    public ApiResponse addNewOne(@RequestBody AddNewOneCommand addNewOneCommand){
        log.info("API: Add doctor to department");
        addNewOneCommandHandler.execute(addNewOneCommand);
        return ApiResponse.success(200, "Thêm thành công");
    }

    @PostMapping("/info")
    @Operation(summary = "Sửa thông tin chuyên khoa")
    public ApiResponse update(@ModelAttribute @Valid UpdateInfoDepartmentCommand command){
        log.info("API: Update department");
        var response= updateInfoDepartmentCommandDepartmentResponseHandler.execute(command);
        return ApiResponse.success(200, "Cập nhập thành công", response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{ids}")
    @Operation(summary = "Xoá")
    public ApiResponse delete(@PathVariable List<String> ids){
        deleteDepartmentHandler.execute(ids);
        return ApiResponse.success(200, "Xoá thành công");
    }

    @PostMapping("/doctor/delete")
    @Operation(summary = "Xoá bác sĩ ra khỏi chuyên khoa")
    public ApiResponse delete(@RequestBody DeleteDoctorDepartmentCommand command){
        deleteDoctorDepartmentCommandHandler.execute(command);
        return ApiResponse.success(200, "Xoá thành công");
    }
}
