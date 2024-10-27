package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.patient.CreateInfoPatientCommand;
import com.example.bookingserver.application.command.command.patient.CreateMedicalHistoryCommand;
import com.example.bookingserver.application.command.command.patient.EmergencyContactCommand;
import com.example.bookingserver.application.command.handle.patient.CreateInfoPatientHandler;
import com.example.bookingserver.application.command.handle.patient.CreateMedicalHistoryHandler;
import com.example.bookingserver.application.command.handle.patient.DeletePatientHandler;
import com.example.bookingserver.application.command.handle.patient.NewEmergencyContactHandler;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.query.handler.patient.FindPatientByUserIdHandler;
import com.example.bookingserver.application.query.handler.patient.GetAllEmergencyContactHandler;
import com.example.bookingserver.application.query.handler.patient.GetAllMedicalHistoryHandler;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/patient")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {

    CreateInfoPatientHandler createInfoPatientHandler;
    NewEmergencyContactHandler newEmergencyContactHandler;
    CreateMedicalHistoryHandler createMedicalHistoryHandler;
    GetAllMedicalHistoryHandler getAllMedicalHistoryHandler;
    GetAllEmergencyContactHandler getAllEmergencyContactHandler;
    FindPatientByUserIdHandler findPatientByUserIdHandler;
    DeletePatientHandler deletePatientHandler;

    @PostMapping()
    @Operation(summary = "Thêm thông tin bệnh nhân")
    public ApiResponse createInfo(@RequestBody @Valid CreateInfoPatientCommand command) {
        var data= createInfoPatientHandler.execute(command);
        return ApiResponse.success(200, "Thêm thông tin bệnh nhân thành công", data);
    }


    @GetMapping("/{userId}")
    @Operation(summary="Lấy ra thông tin bệnh nhân")
    public ApiResponse getInfo(@PathVariable String userId) {
        var response= findPatientByUserIdHandler.execute(userId);
        return ApiResponse.success(200, "Lấy ra của bệnh nhân thành công", response);
    }


    @GetMapping("/history/{patientId}")
    @Operation(summary = "Lấy ra tất cả hồ sơ bệnh án của bệnh nhân")
    public ApiResponse getAllMedicalHistory(
             @PathVariable String patientId
            ,@RequestParam(required = false, defaultValue = "1") Integer pageIndex
            , @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Pageable pageable= getPageable(pageIndex, pageSize);
        var response= getAllMedicalHistoryHandler.execute(patientId, pageable);
        return ApiResponse.success(200, "Lấy ra tất cả tiền án bệnh của bệnh nhân", response);
    }



    @GetMapping("/contact/{patientId}")
    @Operation(summary = "Lấy ra tất cả người liên hệ khẩn cấp theo bệnh nhân")
    public ApiResponse getAllEmergencyContact( @PathVariable String patientId
            ,@RequestParam(required = false, defaultValue = "1") Integer pageIndex
            , @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable= getPageable(pageIndex, pageSize);
        var response= getAllEmergencyContactHandler.execute(patientId, pageable);
        return ApiResponse.success(200, "Lấy ra danh sách liên hệ khẩn cấp của bệnh nhân thành công", response);
    }


    @DeleteMapping("/contact/{contactId}")
    @Operation(summary="Xoá liên hệ khẩn cấp")
    public ApiResponse deleteEmergencyContact(@PathVariable Long contactId) {
        deletePatientHandler.deleteEmergencyContact(contactId);
        return ApiResponse.success(200, "Xoá liên lạc khẩn cấp thành công");
    }

    @DeleteMapping("/history/{historyId}")
    @Operation(summary="Xoá hồ sơ bệnh án")
    public ApiResponse deleteMedicalHistory(@PathVariable Long historyId) {
        deletePatientHandler.deleteMedicalHistory(historyId);
        return ApiResponse.success(200, "Xoá liên lạc khẩn cấp thành công");
    }


    @PostMapping("/contact/{patientId}")
    @Operation(summary="Thêm liên hệ khẩn cấp")
    public ApiResponse createContact(@PathVariable String patientId, @RequestBody @Valid EmergencyContactCommand command) {
        newEmergencyContactHandler.execute(patientId, command);
        return ApiResponse.success(200, "Thêm thông tin liên lạc khẩn cấp thành công");
    }

    @PostMapping("/history")
    @Operation(summary="Thêm hồ sơ bệnh án")
    public ApiResponse createMedicalHistory(@RequestBody @Valid CreateMedicalHistoryCommand command) {
        createMedicalHistoryHandler.execute(command);
        return ApiResponse.success(200, "Thêm hồ sơ bệnh án thành công");
    }


    private Pageable getPageable(Integer pageIndex, Integer pageSize){
        return PageRequest.of(pageIndex-1,pageSize, Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt")));
    }

}
