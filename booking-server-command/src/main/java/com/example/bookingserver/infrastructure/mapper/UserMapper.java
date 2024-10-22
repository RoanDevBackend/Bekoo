package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.command.user.CreateUserCommand;
import com.example.bookingserver.application.command.command.user.UpdateInfoUserCommand;
import com.example.bookingserver.application.command.event.user.CreateUserEvent;
import com.example.bookingserver.application.command.event.user.UpdateAvatarUserEvent;
import com.example.bookingserver.application.command.event.user.UpdateInfoUserEvent;
import com.example.bookingserver.application.command.reponse.UserResponse;
import com.example.bookingserver.domain.Role;
import com.example.bookingserver.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "dob", source = "dob", qualifiedByName = "convertStringToLocalDate")
    User toUserFromCreateCommand(CreateUserCommand command);

    void updateUser(@MappingTarget User user, UpdateInfoUserCommand command);


    UserResponse toResponse(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRoleName")
    CreateUserEvent fromUserToCreateUserEvent(User user);

    UpdateAvatarUserEvent fromUserToUpdateAvatarEvent(User user);

    UpdateInfoUserEvent fromUserToUpdateUserEvent(User user);

    @Named(value = "toRoleName")
    default Set<String> toRoleName(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Named("convertStringToLocalDate")
    default LocalDate toLocalDate(String date){
        return LocalDate.parse(date);
    }
}
