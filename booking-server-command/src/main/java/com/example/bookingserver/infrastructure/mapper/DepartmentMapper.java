package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.event.department.CreateDepartmentEvent;
import com.example.bookingserver.application.event.department.UpdateInfoDepartmentEvent;
import com.example.bookingserver.application.reponse.DepartmentResponse;
import com.example.bookingserver.domain.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(CreateDepartmentCommand command);
    void updateInfo(@MappingTarget Department department, UpdateInfoDepartmentCommand command);
    DepartmentResponse toResponse(Department department);
    CreateDepartmentEvent toCreateDepartmentEvent(Department department);
    UpdateInfoDepartmentEvent toUpdateDepartmentEvent(Department department);
}
