package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.reponse.user.FindByUserIdResponse;
import com.example.bookingserverquery.domain.User;
import document.event.user.CreateUserEvent;
import document.event.user.UpdateInfoUserEvent;
import document.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserFromCreateUserEvent(CreateUserEvent event);
    void updateInfo(@MappingTarget User user, UpdateInfoUserEvent event);
    UserResponse toResponse(User user);
    FindByUserIdResponse toIdResponse(User user);
}
