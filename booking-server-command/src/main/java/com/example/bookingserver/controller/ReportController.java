package com.example.bookingserver.controller;


import com.example.bookingserver.application.command.handle.report.ReportByHandler;
import document.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequestMapping("/report")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
    public ApiResponse total( @RequestParam LocalDate fromDate,
                                @RequestParam LocalDate toDate,
                                @RequestParam int groupType) {

        var response= reportByHandler.executeTotal(fromDate, toDate, groupType);
        return ApiResponse.success(200, "Báo cáo số người đặt khám của toàn hệ thống", response);
    }
}
