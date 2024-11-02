package com.example.bookingserverquery.application.event;

import com.example.bookingserverquery.domain.DoctorDepartment;
import com.example.bookingserverquery.infrastructure.repository.DoctorDepartmentELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import document.event.doctor_department.DoctorDepartmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorDepartmentEventHandler {

    final DoctorDepartmentELRepository doctorDepartmentELRepository;
    final ObjectMapper objectMapper;

    @KafkaListener(topics = TopicConstant.DoctorDepartmentTopic.ADD_NEW_ONE)
    @SneakyThrows
    public void add(String event){
        DoctorDepartmentEvent doctorDepartmentEvent= objectMapper.readValue(event, DoctorDepartmentEvent.class);
        DoctorDepartment doctorDepartment= new DoctorDepartment(UUID.randomUUID().toString(), doctorDepartmentEvent.getDoctorId() , doctorDepartmentEvent.getDepartmentId());
        doctorDepartmentELRepository.save(doctorDepartment);
        log.info("ADD-NEW-ONE-DOCTOR-DEPARTMENT-EVENT: {}", event);
    }

    @KafkaListener(topics = TopicConstant.DoctorDepartmentTopic.DELETE)
    @SneakyThrows
    public void delete(String event){
        DoctorDepartmentEvent doctorDepartmentEvent= objectMapper.readValue(event, DoctorDepartmentEvent.class);
        doctorDepartmentELRepository.deleteByDoctorIdAndDepartmentId(doctorDepartmentEvent.getDoctorId(), doctorDepartmentEvent.getDepartmentId());
    }
}
