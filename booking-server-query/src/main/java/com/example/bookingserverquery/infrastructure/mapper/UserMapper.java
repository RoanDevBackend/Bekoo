package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.event.models.user.CreateUserEvent;
import com.example.bookingserverquery.application.reponse.user.FindByNameResponse;
import com.example.bookingserverquery.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserFromCreateUserEvent(CreateUserEvent event);
}
