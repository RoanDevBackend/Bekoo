package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.FindByPatientResponse;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.domain.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(target = "status", source = "statusId", qualifiedByName = "convertStatus")
    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "active", source = "statusId", qualifiedByName = "convertStatus")
    FindByPatientResponse toFindByUserResponse(Schedule schedule);

    @Mapping(target = "active", source = "statusId", qualifiedByName = "convertStatus")
    FindByDoctorResponse toFindByDoctorResponse(Schedule schedule);

}
