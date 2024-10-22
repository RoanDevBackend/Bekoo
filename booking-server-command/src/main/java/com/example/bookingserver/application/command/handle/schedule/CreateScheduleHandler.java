package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.application.command.service.MessageService;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.ScheduleMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateScheduleHandler {
    ScheduleRepository scheduleRepository;
    UserRepository userRepository;
    ScheduleMapper scheduleMapper;
    DoctorRepository doctorRepository;
    SpecializeRepository specializeRepository;
    MessageService messageService;


    @SneakyThrows
    @Transactional
    public ScheduleResponse execute(CreateScheduleCommand command){
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        Doctor doctor = doctorRepository.findById(command.getDoctorId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DOCTOR_EXISTED));
        Specialize specialize = specializeRepository.findById(command.getSpecializeId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED));
        LocalDateTime checkIn = LocalDateTime.parse(command.getCheckIn());

        if (checkIn.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Ngày đặt không hợp lệ");
        }

        int personPerDay = this.getPersonPerDay(command.getDoctorId());
        System.out.println(personPerDay);

        if (personPerDay + 1 > doctor.getMaximumPeoplePerDay()) {
            throw new RuntimeException("Bác sĩ đã đạt giới hạn lịch khám trong ngày");
        }

        Schedule schedule = Schedule.builder()
                .note(command.getNote())
                .doctor(doctor)
                .specialize(specialize)
                .user(user)
                .checkIn(checkIn)
                .active(true)
                .build();
        scheduleRepository.save(schedule);

        StringBuilder content = new StringBuilder();
        content.append("<h1> Cảm ơn ");
        content.append(user.getName());
        content.append(" Đã đặt dịch vụ khám của chúng tôi, Sau đây là chi tiết lịch khám của bạn.....</h1>");
        messageService.sendMail(user.getEmail(), content.toString(), true);
        return scheduleMapper.toResponse(schedule);
    }

    private int getPersonPerDay(String doctorId){
        return scheduleRepository.getCountPersonPerDay(doctorId, LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }
}

