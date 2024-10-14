package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.doctor.UpdateInfoDoctorCommand;
import com.example.bookingserver.application.event.doctor.CreateDoctorEvent;
import com.example.bookingserver.application.event.doctor.SetMaximumPeoplePerDayEvent;
import com.example.bookingserver.application.event.doctor.UpdateInfoDoctorEvent;
import com.example.bookingserver.application.reponse.DoctorResponse;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toDoctorFromCreateCommand(CreateDoctorCommand command);
    void updateInfo(@MappingTarget Doctor doctor, UpdateInfoDoctorCommand command);

    @Mapping(source = "doctor.id", target = "id")
    DoctorResponse toResponse(Doctor doctor, User user);
    @Mapping(source = "doctor.user.id", target = "user_id")
    CreateDoctorEvent fromDoctorToCreateDoctorEvent(Doctor doctor);
    UpdateInfoDoctorEvent fromDoctorToUpdateInfoEvent(Doctor doctor);
    SetMaximumPeoplePerDayEvent fromDoctorToSerMaximumEvent(Doctor doctor);

}
