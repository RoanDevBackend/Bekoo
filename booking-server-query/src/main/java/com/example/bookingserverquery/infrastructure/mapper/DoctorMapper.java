package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.event.models.doctor.CreateDoctorEvent;
import com.example.bookingserverquery.application.event.models.doctor.UpdateInfoDoctorEvent;
import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toDoctorFromCreateEvent(CreateDoctorEvent command);
    void updateInfo(@MappingTarget Doctor doctor, UpdateInfoDoctorEvent event);
    @Mapping(source = "doctor.id", target = "id")
    DoctorResponse toResponse(Doctor doctor, User user);
}
