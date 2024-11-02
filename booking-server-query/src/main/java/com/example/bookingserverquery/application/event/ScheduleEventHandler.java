package com.example.bookingserverquery.application.event;


import com.example.bookingserverquery.domain.Doctor;
import com.example.bookingserverquery.domain.Patient;
import com.example.bookingserverquery.domain.Schedule;
import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.repository.DoctorELRepository;
import com.example.bookingserverquery.infrastructure.repository.PatientELRepository;
import com.example.bookingserverquery.infrastructure.repository.ScheduleELRepository;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import document.event.schedule.ScheduleEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleEventHandler {

    ObjectMapper objectMapper;
    ScheduleELRepository scheduleELRepository;
    PatientELRepository patientELRepository;
    SpecializeELRepository specializeELRepository;
    DoctorELRepository doctorELRepository;

    @KafkaListener(topics = TopicConstant.ScheduleTopic.CREATE_SCHEDULE)
    @SneakyThrows
    public void create(String content){
        System.out.println(content);
        ScheduleEvent scheduleEvent= objectMapper.readValue(content, ScheduleEvent.class);

        Patient patient= patientELRepository.findById(scheduleEvent.getPatientId()).orElse(null);
        Specialize specialize = specializeELRepository.findById(scheduleEvent.getSpecializeId()).orElse(null);
        Doctor doctor= doctorELRepository.findById(scheduleEvent.getDoctorId()).orElse(null);

        Schedule schedule= Schedule.builder()
                .id(scheduleEvent.getId())
                .note(scheduleEvent.getNote())
                .specialize(specialize)
                .doctor(doctor)
                .patient(patient)
                .status(scheduleEvent.getStatus())
                .createdAt(scheduleEvent.getCreatedAt())
                .createdAt(scheduleEvent.getCreatedAt())
                .updatedAt(scheduleEvent.getUpdatedAt())
                .build();

        scheduleELRepository.save(schedule);
    }

    @KafkaListener(topics = TopicConstant.ScheduleTopic.DELETE_SCHEDULE)
    @SneakyThrows
    public void delete(String content){
        String id= objectMapper.readValue(content, String.class);
        scheduleELRepository.deleteById(id);
    }

}
