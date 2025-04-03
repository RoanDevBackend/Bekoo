package com.example.bookingserverquery.infrastructure.mapper;

import com.example.bookingserverquery.application.reponse.doctor.DoctorResponse;
import com.example.bookingserverquery.domain.Doctor;
import document.event.doctor.CreateDoctorEvent;
import document.event.doctor.UpdateInfoDoctorEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toDoctorFromCreateEvent(CreateDoctorEvent command);
    void updateInfo(@MappingTarget Doctor doctor, UpdateInfoDoctorEvent event);

    @Named("convertDescription")
    default String convertDescription(String description){
        if(description == null || description.isBlank()) return "N/A";
        return description;
    }
    @Named("convertAboutAddress")
    default String convertAboutAddress(String aboutAddress){
        if(aboutAddress == null || aboutAddress.isBlank()) return "N/A";
        return aboutAddress;
    }
    @Mapping(target = "description", source = "description", qualifiedByName = "convertDescription")
    @Mapping(target = "user.aboutAddress" , source = "user.aboutAddress" , qualifiedByName = "convertAboutAddress")
    DoctorResponse toResponse(Doctor doctor);
}
