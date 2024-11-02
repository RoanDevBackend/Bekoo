package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.domain.Department;
import document.event.department.CreateDepartmentEvent;
import document.event.department.UpdateInfoDepartmentEvent;
import document.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(CreateDepartmentEvent event);

    void updateInfo(@MappingTarget Department department, UpdateInfoDepartmentEvent event);

    DepartmentResponse toResponse(Department department);

}
