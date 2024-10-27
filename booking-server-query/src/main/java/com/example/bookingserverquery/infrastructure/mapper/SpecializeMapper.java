package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.event.models.specialize.SpecializeEvent;
import com.example.bookingserverquery.domain.Specialize;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecializeMapper {
    Specialize toSpecialize(SpecializeEvent event);
}
