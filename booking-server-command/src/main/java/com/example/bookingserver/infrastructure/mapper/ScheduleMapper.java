package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.FindByPatientResponse;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.domain.Schedule;
import document.constant.ApplicationConstant;
import document.event.schedule.ScheduleEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    @Named(value = "convertStatus")
    default String convertStatus(int statusId) {
        if(statusId == 1) return "Đã xác nhận";
        if(statusId == 2) return "Đã huỷ" ;
        if(statusId == 3) return "Đã khám";
        return "Quá hạn";
    }

    @Named(value = "convertPaymentStatus")
    default String convertPaymentStatus(int paymentMethod) {
        if(paymentMethod == ApplicationConstant.PaymentMethod.CREDIT) return "Đã thanh toán";
        return "Chưa thanh toán";
    }

    @Mapping(target = "status", source = "statusId", qualifiedByName = "convertStatus")
    @Mapping(target = "paymentStatus", source = "paymentStatus", qualifiedByName = "convertPaymentStatus")
    ScheduleResponse toResponse(Schedule schedule);

    @Mappings({
            @Mapping(target = "doctorId", source = "doctor.id")
            , @Mapping(target = "patientId", source = "patient.id")
            , @Mapping(target = "specializeId", source = "specialize.id")
            , @Mapping(target = "status", source = "statusId", qualifiedByName = "convertStatus")
    })
    ScheduleEvent toEvent(Schedule schedule);

    @Mapping(target = "active", source = "statusId", qualifiedByName = "convertStatus")
    @Mapping(target = "paymentStatus", source = "paymentStatus", qualifiedByName = "convertPaymentStatus")
    FindByPatientResponse toFindByUserResponse(Schedule schedule);

    @Mapping(target = "active", source = "statusId", qualifiedByName = "convertStatus")
    @Mapping(target = "paymentStatus", source = "paymentStatus", qualifiedByName = "convertPaymentStatus")
    FindByDoctorResponse toFindByDoctorResponse(Schedule schedule);

}
