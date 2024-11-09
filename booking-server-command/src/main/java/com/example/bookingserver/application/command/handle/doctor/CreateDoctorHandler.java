package com.example.bookingserver.application.command.handle.doctor;

import com.example.bookingserver.application.command.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.DoctorResponse;
import com.example.bookingserver.application.command.service.PasswordService;
import com.example.bookingserver.domain.*;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.DoctorMapper;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.doctor.CreateDoctorEvent;
import document.event.user.CreateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateDoctorHandler implements HandlerDTO<CreateDoctorCommand, DoctorResponse> {

    final DoctorRepository doctorRepository;
    final MessageProducer messageProducer;
    final DoctorMapper doctorMapper;
    final String TOPIC= TopicConstant.DoctorTopic.CREATE;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    @SneakyThrows
    public DoctorResponse execute(CreateDoctorCommand command) {
        if(!command.getUser().getConfirmPassword().equals(command.getUser().getPassword())){
            throw new BookingCareException(ErrorDetail.ERR_PASSWORD_NOT_CONFIRM);
        }
        if(userRepository.isEmailExisted(command.getUser().getEmail())){
            throw new BookingCareException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }
        User user= userMapper.toUserFromCreateCommand(command.getUser());
        user.setPassword(passwordService.encode(user.getPassword()));
        Set<Role> roles= new HashSet<>();
        roles.add(new Role(ERole.DOCTOR));
        user.setRoles(roles);
        user= userRepository.save(user);

        Doctor doctor= doctorMapper.toDoctorFromCreateCommand(command);
        doctor.setUser(user);
        doctorRepository.save(doctor);

        CreateDoctorEvent mainEvent= doctorMapper.fromDoctorToCreateDoctorEvent(doctor);
        CreateUserEvent userEvent= userMapper.fromUserToCreateUserEvent(user);
        mainEvent.setUser(userEvent);

        messageProducer.sendMessage(TOPIC, "CREATE", mainEvent, mainEvent.getId(), "Doctor");
        return doctorMapper.toResponse(doctor);
    }
}
