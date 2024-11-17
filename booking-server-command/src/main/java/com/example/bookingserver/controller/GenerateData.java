package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.service.GPTService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class GenerateData {

    final GPTService gptService;

    @PostMapping
    public ApiResponse generateData(@RequestParam int total) {
        gptService.addDoctor(total);
        return ApiResponse.success(200, "Thêm dữ liệu cho bác sĩ thành công");
    }
}
