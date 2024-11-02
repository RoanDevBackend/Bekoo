package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import document.event.doctor.CreateDoctorEvent;
import document.event.doctor.UpdateInfoDoctorEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toDoctorFromCreateEvent(CreateDoctorEvent command);
    void updateInfo(@MappingTarget Doctor doctor, UpdateInfoDoctorEvent event);
    DoctorResponse toResponse(Doctor doctor);
}
