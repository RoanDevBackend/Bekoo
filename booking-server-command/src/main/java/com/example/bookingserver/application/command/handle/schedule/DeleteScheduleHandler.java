package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.domain.repository.ScheduleRepository;
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

    public void execute(String id){
        scheduleRepository.delete(id);
    }
}
