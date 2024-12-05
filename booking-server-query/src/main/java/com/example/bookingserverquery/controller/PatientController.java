package com.example.bookingserverquery.controller;

import com.example.bookingserverquery.application.handler.patient.FindPatientByUserIdHandler;
import com.example.bookingserverquery.application.handler.patient.GetAllEmergencyContactHandler;
import com.example.bookingserverquery.application.handler.patient.GetAllMedicalHistoryHandler;
import com.example.bookingserverquery.application.query.QueryBase;
import document.response.ApiResponse;
import document.response.EmergencyContactResponse;
import document.response.MedicalHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/patient")
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class PatientController {

    FindPatientByUserIdHandler findPatientByUserIdHandler;
    GetAllEmergencyContactHandler getAllEmergencyContactHandler;
    GetAllMedicalHistoryHandler getAllMedicalHistoryHandler;

    @GetMapping("/{userId}")
    @Operation(summary="Lấy ra thông tin bệnh nhân")
    public ApiResponse getInfo(@PathVariable String userId) {
        var response= findPatientByUserIdHandler.execute(userId);
        return ApiResponse.success(200, "Lấy ra thông tin của bệnh nhân thành công", response);
    }

    @GetMapping("/history/{patientId}")
    @Operation(summary = "Lấy ra tất cả hồ sơ bệnh án của bệnh nhân")
    public ApiResponse getAllMedicalHistory(
            @PathVariable String patientId
            ,@RequestParam(required = false, defaultValue = "1") Integer pageIndex
            , @RequestParam(required = false, defaultValue = "10000") Integer pageSize){
        var query= QueryBase.<MedicalHistoryResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= getAllMedicalHistoryHandler.execute(patientId, query);
        return ApiResponse.success(200, "Lấy ra tất cả tiền án bệnh của bệnh nhân", response);
    }

    @GetMapping("/contact/{patientId}")
    @Operation(summary = "Lấy ra tất cả người liên hệ khẩn cấp theo bệnh nhân")
    public ApiResponse getAllEmergencyContact( @PathVariable String patientId
            ,@RequestParam(required = false, defaultValue = "1") Integer pageIndex
            , @RequestParam(required = false, defaultValue = "10000") Integer pageSize) {
        var query= QueryBase.<EmergencyContactResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        var response= getAllEmergencyContactHandler.execute(patientId, query);
        return ApiResponse.success(200, "Lấy ra danh sách liên hệ khẩn cấp của bệnh nhân thành công", response);
    }

}
