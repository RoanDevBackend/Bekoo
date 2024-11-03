package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.event.schedule.ScheduleEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.ScheduleResponse;
import com.example.bookingserver.application.command.service.MessageService;
import com.example.bookingserver.domain.*;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.ScheduleMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import document.constant.TopicConstant;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CreateScheduleHandler {
    private ScheduleRepository scheduleRepository;
    private ScheduleMapper scheduleMapper;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private SpecializeRepository specializeRepository;
    private MessageProducer messageProducer;
    private MessageService messageService;
    private SpringTemplateEngine templateEngine;
    String TOPIC= TopicConstant.ScheduleTopic.CREATE_SCHEDULE;


    @Transactional
    @SneakyThrows
    public ScheduleResponse execute(CreateScheduleCommand command){
        Patient patient= patientRepository.findById(command.getPatientId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_PATIENT_NOT_EXISTED));
        Doctor doctor = doctorRepository.findById(command.getDoctorId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DOCTOR_NOT_EXISTED));
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
                .patient(patient)
                .checkIn(checkIn)
                .build();
        scheduleRepository.save(schedule);

        ScheduleEvent event= scheduleMapper.toEvent(schedule);

        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.ADD, event, schedule.getId(), "Schedule");

        DecimalFormat decimalFormat= new DecimalFormat("#,###");
        String cost= decimalFormat.format(schedule.getSpecialize().getPrice());

        List<String> departmentNames= new ArrayList<>();

        for(DoctorDepartment department: schedule.getDoctor().getDoctorDepartments()){
            departmentNames.add(department.getDepartment().getName());
        }
        StringBuilder departmentName= new StringBuilder();
        if(departmentNames.isEmpty()){
            departmentName = new StringBuilder("Bác sĩ này hiện chưa thuộc khoa nào");
        }
        for(int i =0 ;i < departmentNames.size(); i++){
            departmentName.append(departmentNames.get(i));
            if(i != departmentNames.size()-1) {
                departmentName.append(", ");
            }
        }

        Context context = new Context();
        context.setVariable("schedule", schedule);
        context.setVariable("date", schedule.getCheckIn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        context.setVariable("time", schedule.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm")));
        context.setVariable("cost", cost);
        context.setVariable("department", departmentName);

        String subject= "Xác nhận lịch đặt khám";
        String content = templateEngine.process("confirm-schedule", context);

        messageService.sendMail(subject, patient.getUser().getEmail(), content, true);
        return scheduleMapper.toResponse(schedule);
    }

    private int getPersonPerDay(String doctorId){
        return scheduleRepository.getCountPersonPerDay(doctorId, LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }
}

