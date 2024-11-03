package com.example.bookingserver.application.command.handle.doctor;

import com.example.bookingserver.application.command.command.doctor.UpdateInfoDoctorCommand;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.DoctorResponse;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.infrastructure.mapper.DoctorMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.doctor.UpdateInfoDoctorEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateInfoDoctorHandler implements HandlerDTO<UpdateInfoDoctorCommand, DoctorResponse> {

    final DoctorRepository doctorRepository;
    final MessageProducer messageProducer;
    final DoctorMapper doctorMapper;
    final String TOPIC= TopicConstant.DoctorTopic.UPDATE_INFO;

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
        messageProducer.sendMessage(TOPIC, "UPDATE INFO", event, event.getId(), "Doctor");

        return doctorMapper.toResponse(doctor);
    }
}
