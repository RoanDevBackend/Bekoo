package com.example.bookingserver.application.scheduler;

import com.example.bookingserver.application.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class EventScheduler {

    final OutboxEventService outboxEventService;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void send(){
        outboxEventService.execute();
    }
}
