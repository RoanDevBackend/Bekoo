package com.example.bookingserver.infrastructure.mapper;

import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.domain.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Named(value = "getType")
    default int getType(String senderId){
        if(senderId == null) return 1;
        return 0;
    }

    @Mapping(target = "type", source = "senderId", qualifiedByName = "getType")
    ChatBotResponse toResponse(Message message);
}
