package com.example.bookingserver.application.command.handle.doctor;

import com.example.bookingserver.application.command.command.doctor.SetMaximumPeoplePerDayCommand;
import com.example.bookingserver.application.command.event.doctor.SetMaximumPeoplePerDayEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.DoctorMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SetMaximumPeoplePerDayHandler implements Handler<SetMaximumPeoplePerDayCommand> {

    final DoctorRepository doctorRepository;
    final OutboxEventRepository outboxEventRepository;
    final MessageProducer messageProducer;
    final ObjectMapper objectMapper;
    final String TOPIC= TopicConstant.DoctorTopic.SET_PERSON_PER_DAY;

    @Override
    @SneakyThrows
    public void execute(SetMaximumPeoplePerDayCommand command) {
        Doctor doctor= doctorRepository.findById(command.getId())
                .orElseThrow(
                        () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
                );
        doctor.setMaximumPeoplePerDay(command.getValue());
        doctor= doctorRepository.save(doctor);

        SetMaximumPeoplePerDayEvent event= new SetMaximumPeoplePerDayEvent();
        event.setId(doctor.getId());
        event.setValue(command.getValue());
        event.setUpdatedAt(doctor.getUpdatedAt());

        messageProducer.sendMessage(TOPIC, "UPDATE INFO", event, event.getId(), "Doctor");
    }
}
