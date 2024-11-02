package com.example.bookingserverquery.application.event;

import com.example.bookingserverquery.domain.Department;
import com.example.bookingserverquery.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserverquery.infrastructure.repository.DepartmentELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import document.event.department.CreateDepartmentEvent;
import document.event.department.UpdateInfoDepartmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentEventHandler {

    final DepartmentMapper departmentMapper;
    final DepartmentELRepository departmentELRepository;
    final ObjectMapper objectMapper;

    @KafkaListener(topics = TopicConstant.DepartmentTopic.CREATE_DEPARTMENT)
    @SneakyThrows
    public void createDepartment(String event){
        CreateDepartmentEvent createDepartmentEvent= objectMapper.readValue(event, CreateDepartmentEvent.class);
        Department department= departmentMapper.toDepartment(createDepartmentEvent);
        departmentELRepository.save(department);
        log.info("CREATE-DEPARTMENT-EVENT-SUCCESS: {}", event);
    }

    @KafkaListener(topics = TopicConstant.DepartmentTopic.UPDATE_DEPARTMENT)
    @SneakyThrows
    public void updateInfoDepartmentEvent(String event){
        UpdateInfoDepartmentEvent updateInfoDepartmentEvent= objectMapper.readValue(event, UpdateInfoDepartmentEvent.class);
        Department department= departmentELRepository.findById(updateInfoDepartmentEvent.getId())
                .orElse(null);
        if(department == null){
            log.error("ERR-UPDATE-INFO-EVENT: {}", event);
        }else {
            departmentMapper.updateInfo(department, updateInfoDepartmentEvent);
            departmentELRepository.save(department);
            log.info("UPDATE-INFO-EVENT-SUCCESS: {}", event);
        }
    }

    @KafkaListener(topics = TopicConstant.DepartmentTopic.DELETE_DEPARTMENT)
    @SneakyThrows
    public void deleteDepartment(String event){
        String id= objectMapper.readValue(event, String.class);
        if(departmentELRepository.findById(id).isEmpty()){
            log.error("ERR-DELETE-DEPARTMENT-EVENT: {}", id);
        }else {
            departmentELRepository.deleteById(id);
            log.info("DELETE-DEPARTMENT-EVENT-SUCCESS: {}", id);
        }
    }
}
