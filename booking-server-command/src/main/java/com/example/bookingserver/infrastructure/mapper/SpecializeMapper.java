package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Specialize;
import document.event.specialize.SpecializeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpecializeMapper {
    Specialize toSpecialize(CreateSpecializeCommand command);
    SpecializeResponse toResponse(Specialize specialize);
    void updateInfo(@MappingTarget Specialize specialize, UpdateSpecializeCommand command);

    SpecializeEvent toSpecializeEvent(Specialize specialize);
}
