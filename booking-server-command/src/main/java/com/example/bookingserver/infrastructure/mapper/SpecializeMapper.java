package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface SpecializeMapper {
    Specialize toSpecialize(CreateSpecializeCommand command);
    SpecializeResponse toResponse(Specialize specialize);
    void updateInfo(@MappingTarget Specialize specialize, UpdateSpecializeCommand command);
}
