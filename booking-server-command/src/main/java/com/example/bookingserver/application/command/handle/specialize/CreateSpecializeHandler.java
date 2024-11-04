package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.command.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.specialize.SpecializeEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CreateSpecializeHandler {
    SpecializeRepository specializeRepository;
    DepartmentRepository departmentRepository;
    SpecializeMapper specializeMapper;
    MessageProducer messageProducer;
    String TOPIC= TopicConstant.SpecializeTopic.CREATE_SPECIALIZE;

    @SneakyThrows
    @Transactional
    public SpecializeResponse execute(CreateSpecializeCommand command){
        Department department= departmentRepository.findById(command.getDepartmentId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED)
        );

        Specialize specialize= specializeMapper.toSpecialize(command);
        specialize.setDepartment(department);
        specialize= specializeRepository.save(specialize);

        SpecializeEvent event= specializeMapper.toSpecializeEvent(specialize);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.ADD, event, event.getId(), "Specialize");
        return specializeMapper.toResponse(specialize);
    }
}
