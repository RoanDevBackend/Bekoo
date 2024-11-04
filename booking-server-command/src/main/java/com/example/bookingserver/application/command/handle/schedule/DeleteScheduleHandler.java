package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DeleteScheduleHandler {
    private ScheduleRepository scheduleRepository;
    private MessageProducer messageProducer;
    String TOPIC= TopicConstant.ScheduleTopic.DELETE_SCHEDULE;

    public void execute(String id){
        scheduleRepository.delete(id);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.DELETE, id, id, "Schedule");
    }
}
