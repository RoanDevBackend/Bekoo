package com.example.bookingserverquery.application.event;

import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.infrastructure.mapper.DoctorMapper;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import com.example.bookingserverquery.infrastructure.repository.UserELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import document.event.doctor.CreateDoctorEvent;
import document.event.doctor.DeleteDoctorEvent;
import document.event.doctor.SetMaximumPeoplePerDayEvent;
import document.event.doctor.UpdateInfoDoctorEvent;
import document.event.user.CreateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DoctorEventHandler {

    final ObjectMapper objectMapper;
    final DoctorELRepository doctorELRepository;
    final UserELRepository userELRepository;
    final DoctorMapper doctorMapper;
    final UserMapper userMapper;

    @KafkaListener(topics = TopicConstant.DoctorTopic.CREATE)
    @SneakyThrows
    public void createDoctorEvent(String event){
        CreateDoctorEvent doctorEvent= objectMapper.readValue(event, CreateDoctorEvent.class);
        CreateUserEvent userEvent= doctorEvent.getUser();

        User user= userMapper.toUserFromCreateUserEvent(userEvent);
        userELRepository.save(user);

        Doctor doctor= doctorMapper.toDoctorFromCreateEvent(doctorEvent);
        doctor.setUser(user);
        doctorELRepository.save(doctor);
        log.info("CREATE-DOCTOR-EVENT SUCCESS: {}", event);
    }

    @KafkaListener(topics = TopicConstant.DoctorTopic.UPDATE_INFO)
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

    @KafkaListener(topics = TopicConstant.DoctorTopic.SET_PERSON_PER_DAY)
    @SneakyThrows
    public void abc(String event){
        SetMaximumPeoplePerDayEvent setMaximumPeoplePerDayEvent= objectMapper.readValue(event, SetMaximumPeoplePerDayEvent.class);
        Optional<Doctor> doctorOptional= doctorELRepository.findById(setMaximumPeoplePerDayEvent.getId());
        if(doctorOptional.isEmpty()){ return; }
        Doctor doctor= doctorOptional.get();
        doctor.setMaximumPeoplePerDay(setMaximumPeoplePerDayEvent.getValue());
        doctor.setUpdatedAt(setMaximumPeoplePerDayEvent.getUpdatedAt());
        doctorELRepository.save(doctor);
        log.info("SET-MAXIMUM-PEOPLE-PER-DAY-EVENT: {}", event);
    }

    @KafkaListener(topics = TopicConstant.DoctorTopic.DELETE)
    @SneakyThrows
    public void deleteDoctorEvent(String event){
        DeleteDoctorEvent deleteDoctorEvent= objectMapper.readValue(event, DeleteDoctorEvent.class);
        doctorELRepository.deleteById(deleteDoctorEvent.getId());
        log.info("DELETE-DOCTOR-EVENT-SUCCESS: {}", event);
    }

}
