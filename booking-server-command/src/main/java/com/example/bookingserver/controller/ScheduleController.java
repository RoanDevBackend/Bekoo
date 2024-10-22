package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.schedule.CreateScheduleHandler;
import com.example.bookingserver.application.command.handle.schedule.DeleteScheduleHandler;
import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByDoctorHandler;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByUserHandler;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.query.handler.response.FindByUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;


@RequestMapping(value = "/schedule")
@RestController
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class ScheduleController {
    CreateScheduleHandler createScheduleHandler;
    DeleteScheduleHandler deleteScheduleHandler;
    FindHistoryScheduleByUserHandler findHistoryScheduleByUserHandler;
    FindHistoryScheduleByDoctorHandler findHistoryScheduleByDoctorHandler;

    @PostMapping
    public ApiResponse create(@RequestBody @Valid CreateScheduleCommand command){
        var response = createScheduleHandler.execute(command);
        return ApiResponse.success(201, "Tạo thành công", response);
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse delete(@PathVariable String id){
        deleteScheduleHandler.execute(id);
        return ApiResponse.success(200, "Xoá thành công");
    }

    @Operation(summary = "Tìm kiếm lịch sử đặt lịch khám của người dùng")
    @PostMapping("/user/{id}")
    public ApiResponse getUserSchedule(@PathVariable String id, @RequestBody(required = false)@Valid QueryBase<FindByUserResponse> queryBase){
        if(queryBase == null){
            queryBase = QueryBase.<FindByUserResponse>builder().build();
        }
        var response = findHistoryScheduleByUserHandler.findByUser(id, queryBase);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Tìm kiếm lịch sử đặt lịch khám của bác sĩ")
    @PostMapping("/doctor/{id}")
    public ApiResponse getDoctorSchedule(@PathVariable String id, @RequestBody(required = false)@Valid QueryBase<FindByDoctorResponse> queryBase){
        if(queryBase == null){
            queryBase = QueryBase.<FindByDoctorResponse>builder().build();
        }
        LocalDateTime start= LocalDateTime.now().minusYears(200);
        LocalDateTime end= LocalDateTime.now().plusYears(200);
        System.out.println("Start: " + start.toString() + "\nEnd: " + end.toString());
        var response = findHistoryScheduleByDoctorHandler.execute(id, queryBase, start, end);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Tìm kiếm lịch lịch khám của bác sĩ theo ngày")
    @PostMapping("/doctor/day/{id}")
    public ApiResponse getDoctorSchedulePerDay(@PathVariable String id, @RequestBody(required = false)@Valid QueryBase<FindByDoctorResponse> queryBase){
        if(queryBase == null){
            queryBase = QueryBase.<FindByDoctorResponse>builder().build();
        }
        LocalDateTime start= LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end= start.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        var response = findHistoryScheduleByDoctorHandler.execute(id, queryBase, start, end);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }
}
