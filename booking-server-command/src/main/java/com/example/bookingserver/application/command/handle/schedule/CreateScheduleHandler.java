package com.example.bookingserver.application.command.handle.schedule;


import com.example.bookingserver.application.command.command.schedule.CreateScheduleCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.service.MessageService;
import com.example.bookingserver.application.command.service.OnlinePayService;
import com.example.bookingserver.domain.*;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.mapper.ScheduleMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.example.bookingserver.infrastructure.persistence.repository.PatientRepository;
import document.constant.ApplicationConstant;
import document.constant.TopicConstant;
import document.event.schedule.ScheduleEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CreateScheduleHandler {
    OnlinePayService onlinePayService;
    ScheduleRepository scheduleRepository;
    ScheduleMapper scheduleMapper;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    SpecializeRepository specializeRepository;
    MessageProducer messageProducer;
    MessageService messageService;
    SpringTemplateEngine templateEngine;
    String TOPIC= TopicConstant.ScheduleTopic.CREATE_SCHEDULE;


    @Transactional
    @SneakyThrows
    public String execute(CreateScheduleCommand command, HttpServletRequest request) {
        Patient patient= patientRepository.findById(command.getPatientId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_PATIENT_NOT_EXISTED));
        Doctor doctor = doctorRepository.findById(command.getDoctorId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DOCTOR_NOT_EXISTED));
        Specialize specialize = specializeRepository.findById(command.getSpecializeId())
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED));

        LocalDateTime checkIn = LocalDateTime.parse(command.getCheckIn());

        if (checkIn.isBefore(LocalDateTime.now())) {
            throw new BookingCareException(ErrorDetail.ERR_SCHEDULE_DATE);
        }

        int personPerDay = this.getPersonPerDay(command.getDoctorId());
        System.out.println(personPerDay);

        if (personPerDay + 1 > doctor.getMaximumPeoplePerDay()) {
            throw new BookingCareException(ErrorDetail.ERR_DOCTOR_PERSON);
        }

        Schedule schedule = Schedule.builder()
                .note(command.getNote())
                .doctor(doctor)
                .specialize(specialize)
                .patient(patient)
                .checkIn(checkIn)
                .paymentStatus(ApplicationConstant.PaymentMethod.CASH)
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

        if(command.getPaymentMethod() == ApplicationConstant.PaymentMethod.CASH){
            return null;
        }else{
            return onlinePayService.payCart(schedule.getId(), request);
        }
    }

    private int getPersonPerDay(String doctorId){
        return scheduleRepository.getCountPersonPerDay(doctorId, LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }
}

