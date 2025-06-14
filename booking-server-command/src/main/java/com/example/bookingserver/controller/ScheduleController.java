package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.schedule.CreateScheduleHandler;
import com.example.bookingserver.application.command.handle.schedule.DeleteScheduleHandler;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.application.command.service.OnlinePayService;
import com.example.bookingserver.application.query.handler.response.FindByPatientResponse;
import com.example.bookingserver.application.query.handler.schedule.FindAllScheduleHandler;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByDoctorHandler;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByPatientHandler;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.query.handler.schedule.GetAvailableTimeByDoctorHandler;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RequestMapping(value = "/schedule")
@RestController
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class ScheduleController {
    CreateScheduleHandler createScheduleHandler;
    DeleteScheduleHandler deleteScheduleHandler;
    FindHistoryScheduleByPatientHandler findHistoryScheduleByPatientHandler;
    FindHistoryScheduleByDoctorHandler findHistoryScheduleByDoctorHandler;
    FindAllScheduleHandler findAllScheduleHandler;
    OnlinePayService onlinePayService;
    GetAvailableTimeByDoctorHandler getAvailableTimeByDoctorHandler;

    @Operation(summary = "Đặt lịch khám", parameters = {
            @Parameter(name = "paymentMethod", description = "1 Thanh toán khi tới khám. 2 Thanh toán bằng thẻ tín dụng")
    })
    @PostMapping
    public ApiResponse create(@RequestBody @Valid CreateScheduleCommand command, HttpServletRequest request) {
        var response = createScheduleHandler.execute(command, request);
        return ApiResponse.success(201, "Đặt lịch khám thành công", response);
    }

    @Operation(summary = "Xoá lịch đặt khám")
    @DeleteMapping(value = "/{id}")
    public ApiResponse delete(@PathVariable String id){
        deleteScheduleHandler.execute(id);
        return ApiResponse.success(200, "Đã xác nhận huỷ khám");
    }

    @Operation(summary = "Lấy ra danh sách tất cả lịch khám")
    @GetMapping()
    public ApiResponse getAll(@RequestParam(required = false, defaultValue = "1") int pageIndex
            , @RequestParam(required = false, defaultValue = "10000") int pageSize ){
        QueryBase<ScheduleResponse> queryBase= QueryBase.<ScheduleResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response = findAllScheduleHandler.execute(queryBase);
        return ApiResponse.success(200, "Tìm kiếm lịch sử lịch khám của bệnh nhân", response);
    }


    @Operation(summary = "Tìm kiếm lịch sử đặt lịch khám của bệnh nhân", parameters = {
            @Parameter(name = "id", description = "Mã bệnh nhân")
            , @Parameter(name = "statusId", description = "1 -Lấy ra tất cả. 2 -Lấy ra lịch khám đã huỷ. 3 -Đã tới khám.  4- Quá hạn chưa tới khám. 0- Lấy ra tất cả")
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
    @GetMapping("/doctor/{id}/{statusId}")
    public ApiResponse getDoctorSchedule(@PathVariable String id, @PathVariable int statusId
            , @RequestParam(required = false, defaultValue = "1") int pageIndex
            , @RequestParam(required = false, defaultValue = "10000") int pageSize ){
        Pageable pageable= PageRequest.of(pageIndex - 1,pageSize, Sort.by("checkIn").ascending());
        LocalDateTime start= LocalDateTime.now().minusYears(200);
        LocalDateTime end= LocalDateTime.now().plusYears(200);
        var response = findHistoryScheduleByDoctorHandler.execute(id, pageable, start, end, statusId);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Tìm kiếm lịch khám của bác sĩ ngày hôm nay", parameters = {
            @Parameter(name = "id", description = "Mã bác sĩ")
    })
    @GetMapping("/doctor/day/{id}")
    public ApiResponse getDoctorSchedulePerDay(@PathVariable String id
            , @RequestParam(required = false, defaultValue = "1") int pageIndex
            , @RequestParam(required = false, defaultValue = "10000") int pageSize){
        Pageable pageable= PageRequest.of(pageIndex - 1,pageSize, Sort.by("checkIn").ascending());
        LocalDateTime start= LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end= start.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        var response = findHistoryScheduleByDoctorHandler.execute(id, pageable, start, end, ApplicationConstant.Status.CONFIRMED);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Lấy ra trạng thái thời gian của bác sĩ", parameters = {@Parameter(name="date" , description = "yyyy-mm-dd") })
    @GetMapping("/doctor/time")
    public ApiResponse getAvailableTime(@RequestParam String doctorId, @RequestParam String date){
        var response = getAvailableTimeByDoctorHandler.execute(doctorId, date);
        return ApiResponse.success(200, "" , response);
    }

    @Operation(summary = "Thanh toán lịch khám, trước đó chưa thanh toán")
    @PostMapping("/payment")
    public ApiResponse payment(@RequestParam String scheduleId, HttpServletRequest request) {
        String url= onlinePayService.payCart(scheduleId, request);
        return ApiResponse.success(200, "Thanh toán bằng thẻ tín dụng", url);
    }

}
