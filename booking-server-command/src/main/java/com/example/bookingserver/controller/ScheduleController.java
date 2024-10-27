package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.schedule.CreateScheduleHandler;
import com.example.bookingserver.application.command.handle.schedule.DeleteScheduleHandler;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.FindByPatientResponse;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByDoctorHandler;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByPatientHandler;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RequestMapping(value = "/schedule")
@RestController
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class ScheduleController {
    CreateScheduleHandler createScheduleHandler;
    DeleteScheduleHandler deleteScheduleHandler;
    FindHistoryScheduleByPatientHandler findHistoryScheduleByPatientHandler;
    FindHistoryScheduleByDoctorHandler findHistoryScheduleByDoctorHandler;

    @PostMapping
    public ApiResponse create(@RequestBody @Valid CreateScheduleCommand command){
        var response = createScheduleHandler.execute(command);
        return ApiResponse.success(201, "Tạo thành công", response);
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse delete(@PathVariable String id){
        deleteScheduleHandler.execute(id);
        return ApiResponse.success(200, "Đã xác nhận huỷ khám");
    }

    @Operation(summary = "Tìm kiếm lịch sử đặt lịch khám của bệnh nhân", parameters = {
            @Parameter(name = "id", description = "Mã bệnh nhân")
            , @Parameter(name = "statusId", description = "1 -Lấy ra tất cả. 2 -Lấy ra lịch khám đã huỷ. 3 -Đã tới khám.  4- Quá hạn chưa tới khám")
    })
    @GetMapping("/patient/{id}/{statusId}")
    public ApiResponse getPatientSchedule(@PathVariable String id, @PathVariable int statusId
            , @RequestParam(required = false, defaultValue = "1") int pageIndex
            , @RequestParam(required = false, defaultValue = "10000") int pageSize ){
        QueryBase<FindByPatientResponse> queryBase= QueryBase.<FindByPatientResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response = findHistoryScheduleByPatientHandler.execute(id, statusId, queryBase);
        return ApiResponse.success(200, "Tìm kiếm lịch sử lịch khám của bệnh nhân", response);
    }


    @Operation(summary = "Tìm kiếm lịch sử đặt lịch khám của bác sĩ", parameters = {
            @Parameter(name = "id", description = "Mã bác sĩ")
    })
    @GetMapping("/doctor/{id}")
    public ApiResponse getDoctorSchedule(@PathVariable String id
            , @RequestParam(required = false, defaultValue = "1") int pageIndex
            , @RequestParam(required = false, defaultValue = "10000") int pageSize ){
        QueryBase<FindByDoctorResponse> queryBase = QueryBase.<FindByDoctorResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        LocalDateTime start= LocalDateTime.now().minusYears(200);
        LocalDateTime end= LocalDateTime.now().plusYears(200);
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
