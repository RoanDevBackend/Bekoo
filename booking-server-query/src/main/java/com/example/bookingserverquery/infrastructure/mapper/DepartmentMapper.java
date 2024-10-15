package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.event.models.department.CreateDepartmentEvent;
import com.example.bookingserverquery.application.event.models.department.UpdateInfoDepartmentEvent;
import com.example.bookingserverquery.application.reponse.department.DepartmentResponse;
import com.example.bookingserverquery.domain.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(CreateDepartmentEvent event);

    void updateInfo(@MappingTarget Department department, UpdateInfoDepartmentEvent event);

    DepartmentResponse toResponse(Department department);

}
