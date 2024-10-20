package com.example.bookingserver.application.handle.doctor;

import com.example.bookingserver.application.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.user.CreateUserCommand;
import com.example.bookingserver.application.event.doctor.CreateDoctorEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.Handler_DTO;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.handle.user.CreateUserHandler;
import com.example.bookingserver.application.reponse.DoctorResponse;
import com.example.bookingserver.application.reponse.UserResponse;
import com.example.bookingserver.domain.*;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.DoctorMapper;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateDoctorHandler implements Handler_DTO<CreateDoctorCommand, DoctorResponse> {

    final DoctorRepository doctorRepository;
    final OutboxEventRepository outboxEventRepository;
    final MessageProducer messageProducer;
    final DoctorMapper doctorMapper;
    final ObjectMapper objectMapper;
    final String TOPIC="create-doctor-event";
    private final UserMapper userMapper;
    final CreateUserHandler createUserHandler;
    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    public DoctorResponse execute(CreateDoctorCommand command) {

        Set<Role> roles= new HashSet<>();
        roles.add(new Role(ERole.USER));
        roles.add(new Role(ERole.DOCTOR));

        UserResponse userResponse= createUserHandler.execute(command.getUser(), roles);

        User user= userRepository.findById(userResponse.getId()).orElse(null);

        Doctor doctor= doctorMapper.toDoctorFromCreateCommand(command);

        doctor.setUser(user);
        try {
            doctorRepository.save(doctor);
        }catch (Exception e){
            throw new BookingCareException(ErrorDetail.ERR_DOCTOR_EXISTED);
        }

        CreateDoctorEvent event= doctorMapper.fromDoctorToCreateDoctorEvent(doctor);

        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("CREATE")
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
