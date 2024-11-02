package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.command.doctor.UpdateInfoDoctorCommand;
import com.example.bookingserver.application.command.event.doctor.CreateDoctorEvent;
import com.example.bookingserver.application.command.event.doctor.SetMaximumPeoplePerDayEvent;
import com.example.bookingserver.application.command.event.doctor.UpdateInfoDoctorEvent;
import com.example.bookingserver.application.command.reponse.DoctorResponse;
import com.example.bookingserver.domain.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toDoctorFromCreateCommand(CreateDoctorCommand command);
    void updateInfo(@MappingTarget Doctor doctor, UpdateInfoDoctorCommand command);


    DoctorResponse toResponse(Doctor doctor);

    @Mapping(target = "user", ignore = true)
    CreateDoctorEvent fromDoctorToCreateDoctorEvent(Doctor doctor);
    UpdateInfoDoctorEvent fromDoctorToUpdateInfoEvent(Doctor doctor);
}
