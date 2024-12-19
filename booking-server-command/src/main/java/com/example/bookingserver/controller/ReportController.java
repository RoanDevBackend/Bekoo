package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.handle.report.ReportByHandler;
import document.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RequestMapping("/report")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name="bearerAuth")
public class ReportController {

    ReportByHandler reportByHandler;

    @GetMapping("/doctor")
    @Operation(summary = "Báo cáo số người đặt khám của bác sĩ", parameters = {
            @Parameter(name = "groupType", description = "1-Theo ngày, 2-Theo tuần, 3-Theo tháng, 4-Theo năm")
    })
    public ApiResponse byDoctor(@RequestParam String doctorId,
                                @RequestParam LocalDate fromDate,
                                @RequestParam LocalDate toDate,
                                @RequestParam int groupType) {
        var response= reportByHandler.executeByDoctor(doctorId, fromDate, toDate, groupType);
        return ApiResponse.success(200, "Báo cáo số người đặt khám của bác sĩ", response);
    }

    @GetMapping
    @Operation(summary = "Báo cáo số người đặt khám của toàn hệ thống", parameters = {
            @Parameter(name = "groupType", description = "1-Theo ngày, 2-Theo tuần, 3-Theo tháng, 4-Theo năm")
    })
    public ApiResponse total(@RequestParam String fromDate,
                             @RequestParam String toDate,
                             @RequestParam int groupType) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        var response= reportByHandler.executeTotal(from, to, groupType);
        return ApiResponse.success(200, "Báo cáo số người đặt khám của toàn hệ thống", response);
    }

    @GetMapping("/by-age")
    @Operation(summary = "Báo cáo thống kê độ tuổi theo biểu đồ tròn")
    public ApiResponse byAge(){
        var response= reportByHandler.getByAge();
        return ApiResponse.success(200, "Thống kê theo số tuổi dưới dạng biểu đồ tròn", response);
    }

    @GetMapping("/total")
    @Operation(summary = "Báo cáo số lượng tổng thể của toàn hệ thống")
    public ApiResponse total(){
        var response= reportByHandler.execute();
        return ApiResponse.success(200, "Báo cáo số lượng tổng thể của toàn hệ thống", response);
    }
}
