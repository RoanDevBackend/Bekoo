package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.domain.Specialize;
import document.event.specialize.SpecializeEvent;
import document.response.SpecializeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecializeMapper {
    Specialize toSpecialize(SpecializeEvent event);
    SpecializeResponse toResponse(Specialize specialize);
}
