package com.example.bookingserverquery.application.event.handler;

import com.example.bookingserverquery.application.event.models.doctor.CreateDoctorEvent;
import com.example.bookingserverquery.application.event.models.doctor.DeleteDoctorEvent;
import com.example.bookingserverquery.application.event.models.doctor.SetMaximumPeoplePerDayEvent;
import com.example.bookingserverquery.application.event.models.doctor.UpdateInfoDoctorEvent;
import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DoctorEventHandler {

    final ObjectMapper objectMapper;
    final DoctorELRepository doctorELRepository;
    final UserELRepository userELRepository;
    final DoctorMapper doctorMapper;


    @KafkaListener(topics = "create-doctor-event")
    @SneakyThrows
    public void createDoctorEvent(String event){
        CreateDoctorEvent createDoctorEvent= objectMapper.readValue(event, CreateDoctorEvent.class);
        Doctor doctor= doctorMapper.toDoctorFromCreateEvent(createDoctorEvent);
        User user= userELRepository.findById(createDoctorEvent.getUser_id()).orElse(null);
        doctor.setUser(user);
        doctorELRepository.save(doctor);
        log.info("CREATE-DOCTOR-EVENT SUCCESS: {}", event);
    }


    @KafkaListener(topics = "update-info-doctor-event")
    @SneakyThrows
    public void updateDoctorEvent(String event){
        UpdateInfoDoctorEvent updateInfoDoctorEvent= objectMapper.readValue(event, UpdateInfoDoctorEvent.class);

        Optional<Doctor> doctorOptional= doctorELRepository.findById(updateInfoDoctorEvent.getId());
        if(doctorOptional.isEmpty()){ return; }
        Doctor doctor= doctorOptional.get();

        doctorMapper.updateInfo(doctor, updateInfoDoctorEvent);
        doctorELRepository.save(doctor);
        log.info("UPDATE-INFO-DOCTOR-EVENT SUCCESS: {}", event);
    }

    @KafkaListener(topics = "set-maximum-people-per-day-event")
    @SneakyThrows
    public void abc(String event){
        SetMaximumPeoplePerDayEvent setMaximumPeoplePerDayEvent= objectMapper.readValue(event, SetMaximumPeoplePerDayEvent.class);
        Optional<Doctor> doctorOptional= doctorELRepository.findById(setMaximumPeoplePerDayEvent.getId());
        if(doctorOptional.isEmpty()){ return; }
        Doctor doctor= doctorOptional.get();
        doctor.setMaximumPeoplePerDay(setMaximumPeoplePerDayEvent.getValue());
        doctorELRepository.save(doctor);
        log.info("SET-MAXIMUM-PEOPLE-PER-DAY-EVENT: {}", event);
    }

    @KafkaListener(topics = "delete-doctor-event")
    @SneakyThrows
    public void deleteDoctorEvent(String event){
        DeleteDoctorEvent deleteDoctorEvent= objectMapper.readValue(event, DeleteDoctorEvent.class);
        doctorELRepository.deleteById(deleteDoctorEvent.getId());
        log.info("DELETE-DOCTOR-EVENT-SUCCESS: {}", event);
    }

}
