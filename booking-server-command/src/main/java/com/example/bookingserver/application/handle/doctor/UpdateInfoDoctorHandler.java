package com.example.bookingserver.application.handle.doctor;

import com.example.bookingserver.application.command.doctor.UpdateInfoDoctorCommand;
import com.example.bookingserver.application.event.doctor.UpdateInfoDoctorEvent;
import com.example.bookingserver.application.handle.Handler_DTO;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.reponse.DoctorResponse;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.DoctorMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateInfoDoctorHandler implements Handler_DTO<UpdateInfoDoctorCommand, DoctorResponse> {

    final DoctorRepository doctorRepository;
    final OutboxEventRepository outboxEventRepository;
    final MessageProducer messageProducer;
    final DoctorMapper doctorMapper;
    final ObjectMapper objectMapper;
    final String TOPIC= "update-info-doctor-event";

    @Override
    @SneakyThrows

    public DoctorResponse execute(UpdateInfoDoctorCommand command) {
        Doctor doctor= doctorRepository.findById(command.getId())
                .orElseThrow(
                        () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
                );
        doctorMapper.updateInfo(doctor, command);
        doctorRepository.save(doctor);

        UpdateInfoDoctorEvent event= doctorMapper.fromDoctorToUpdateInfoEvent(doctor);
        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("UPDATE")
                .aggregateId(doctor.getId())
                .aggregateType("Doctor")
                .content(content)
                .status(ApplicationConstant.EventStatus.PENDING)
                .build();

        try {
            messageProducer.sendMessage(TOPIC, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
            log.info("SEND EVENT SUCCESS: {}", TOPIC);
        }catch (Exception e){
            log.error("SEND EVENT FAILED: {}", TOPIC );
        }
        outboxEventRepository.save(outboxEvent);

        return doctorMapper.toResponse(doctor);
    }
}
