package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteScheduleHandler {
    ScheduleRepository scheduleRepository;
    MessageProducer messageProducer;
    String TOPIC= "delete-schedule";

    public void execute(String id){
        scheduleRepository.delete(id);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.DELETE, id, id, "Schedule");
    }
}
