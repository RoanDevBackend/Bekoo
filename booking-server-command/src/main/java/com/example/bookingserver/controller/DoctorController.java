package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.doctor.DeleteDoctorCommand;
import com.example.bookingserver.application.command.doctor.SetMaximumPeoplePerDayCommand;
import com.example.bookingserver.application.command.doctor.UpdateInfoDoctorCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.Handler_DTO;
import com.example.bookingserver.application.reponse.ApiResponse;
import com.example.bookingserver.application.reponse.DoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/doctor")
@RestController
@RequiredArgsConstructor
public class DoctorController {

    final Handler_DTO<CreateDoctorCommand, DoctorResponse> createDoctorCommandHandler;
    final Handler<DeleteDoctorCommand> deleteDoctorCommandHandler;
    final Handler<SetMaximumPeoplePerDayCommand> setMaximumPeoplePerDayCommandHandler;
    final Handler_DTO<UpdateInfoDoctorCommand, DoctorResponse> updateInfoDoctorCommandHandler;


    @PostMapping
    public ApiResponse create(@RequestBody CreateDoctorCommand command){
        var response= createDoctorCommandHandler.execute(command);
        return ApiResponse.success(HttpStatus.CREATED.value(), "Thêm thông tin thành công", response, HttpStatus.CREATED);
    }

    @PutMapping
    public ApiResponse updateInfo(@RequestBody UpdateInfoDoctorCommand command){
        var response= updateInfoDoctorCommandHandler.execute(command);
        return ApiResponse.success(200, "Sửa thông tin thành công", response);
    }

    @PutMapping(value = "/day")
    public ApiResponse setMaximumPeoplePerDay(@RequestBody SetMaximumPeoplePerDayCommand command){
        setMaximumPeoplePerDayCommandHandler.execute(command);
        return ApiResponse.success(200, "Thay đổi thành công");
    }

    @DeleteMapping
    public ApiResponse delete(@RequestBody DeleteDoctorCommand command){
        deleteDoctorCommandHandler.execute(command);
        return ApiResponse.success(200, "Xoá thành công");
    }



}
