package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.query.handler.response.FindByDoctorResponse;
import com.example.bookingserver.application.query.handler.response.FindByUserResponse;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.domain.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    @Mapping(target = "active", source = "active", qualifiedByName = "getStatus")
    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "active", source = "active", qualifiedByName = "getStatus")
    FindByUserResponse toFindByUserResponse(Schedule schedule);

    @Mapping(target = "active", source = "active", qualifiedByName = "getStatus")
    FindByDoctorResponse toFindByDoctorResponse(Schedule schedule);

    @Named(value= "getStatus")
    default String getStatus(boolean active){
        if(active) return "Đã xác nhận";
        else return "Đã huỷ";
    }
}
