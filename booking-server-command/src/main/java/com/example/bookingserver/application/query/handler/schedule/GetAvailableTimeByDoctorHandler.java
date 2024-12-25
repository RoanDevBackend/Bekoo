package com.example.bookingserver.application.query.handler.schedule;


import com.example.bookingserver.application.query.handler.response.TimeTemplateResponse;
import com.example.bookingserver.infrastructure.persistence.repository.ScheduleJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GetAvailableTimeByDoctorHandler {

    final ScheduleJpaRepository scheduleJpaRepository;

    public Map<String, ?> execute(String doctorId){
        int startHour = 8;
        int startMinute = 0;
        List<TimeTemplateResponse> s1 = new ArrayList<>();
        List<TimeTemplateResponse> s2 = new ArrayList<>();
        Map<String, List<TimeTemplateResponse>> response = new HashMap<>();
        while (startHour < 17){
            LocalDateTime time = LocalDate.now().atTime(startHour, startMinute);
            int numberSchedule = scheduleJpaRepository.countScheduleByDoctorIdAndCheckIn(doctorId, time);
            boolean isAvailable = numberSchedule < 2;
            if(startHour < 12){
                s1.add(new TimeTemplateResponse(startHour  + ":00 - " + (startHour + 1) + ":00", isAvailable ));
            }
            if(startHour >= 13) {
                s2.add(new TimeTemplateResponse(startHour  + ":00 - " + (startHour + 1) + ":00", isAvailable ));
            }
            startHour++;
        }
        response.put("afternoon", s2);
        response.put("morning", s1);
        return response;
    }
}
