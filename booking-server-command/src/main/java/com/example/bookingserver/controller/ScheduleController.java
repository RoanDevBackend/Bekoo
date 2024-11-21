package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.schedule.CreateScheduleHandler;
import com.example.bookingserver.application.command.handle.schedule.DeleteScheduleHandler;
import com.example.bookingserver.application.command.service.OnlinePayService;
import com.example.bookingserver.application.query.handler.response.FindByPatientResponse;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByDoctorHandler;
import com.example.bookingserver.application.query.handler.schedule.FindHistoryScheduleByPatientHandler;
import com.example.bookingserver.application.query.QueryBase;
import com.example.bookingserver.application.command.reponse.ApiResponse;
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
    OnlinePayService onlinePayService;

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
        Pageable pageable= PageRequest.of(pageIndex - 1,pageSize, Sort.by("checkIn").ascending());
        LocalDateTime start= LocalDateTime.now().minusYears(200);
        LocalDateTime end= LocalDateTime.now().plusYears(200);
        var response = findHistoryScheduleByDoctorHandler.execute(id, pageable, start, end, ApplicationConstant.Status.CONFIRMED);
        return ApiResponse.success(200, "Tìm kiếm thành công", response);
    }

    @Operation(summary = "Tìm kiếm lịch lịch khám của bác sĩ ngày hôm nay", parameters = {
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

    @PostMapping("/payment")
    public ApiResponse onlinePayment(@RequestParam String scheduleId, HttpServletRequest request){
        String url= onlinePayService.payCart(scheduleId, request);
        return ApiResponse.success(200, "", url);
    }

    @GetMapping("/payment/result")
    public ApiResponse onlinePaymentResult(@RequestParam String vnp_ResponseCode){
        if(vnp_ResponseCode.equals("00")) return ApiResponse.success(200, "Thanh toán thành công");
        if(vnp_ResponseCode.equals("11")) return ApiResponse.success(200, "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
        if(vnp_ResponseCode.equals("12")) return ApiResponse.success(200, "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
        if(vnp_ResponseCode.equals("13")) return ApiResponse.success(400, "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.");
        if(vnp_ResponseCode.equals("24")) return ApiResponse.success(200, "Giao dịch không thành công do: Khách hàng hủy giao dịch");
        if(vnp_ResponseCode.equals("51")) return ApiResponse.success(200, "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
        if(vnp_ResponseCode.equals("65")) return ApiResponse.success(200, "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
        if(vnp_ResponseCode.equals("75")) return ApiResponse.success(200, "Ngân hàng thanh toán đang bảo trì.");
        if(vnp_ResponseCode.equals("79")) return ApiResponse.success(200, "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch");
        return ApiResponse.success(400, "Bạn hãy thử lại");
    }

}
