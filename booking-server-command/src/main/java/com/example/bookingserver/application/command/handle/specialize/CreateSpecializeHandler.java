package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.command.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.event.specialize.SpecializeEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CreateSpecializeHandler {
    SpecializeRepository specializeRepository;
    DepartmentRepository departmentRepository;
    OutboxEventRepository outboxEventRepository;

    SpecializeMapper specializeMapper;
    ObjectMapper objectMapper;
    MessageProducer messageProducer;
    String TOPIC="create-specialize";

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
        String content= objectMapper.writeValueAsString(event);
        System.out.println(content);
        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("CREATE-SPECIALIZE")
                .aggregateId(specialize.getId())
                .aggregateType("Specialize")
                .content(content)
                .status(ApplicationConstant.EventStatus.PENDING)
                .build();

        try {
            messageProducer.sendMessage(TOPIC, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
        }catch (Exception e){
            log.error("SEND EVENT CREATE SPECIALIZE FAILED: {}", TOPIC );
        }
        outboxEventRepository.save(outboxEvent);
        return specializeMapper.toResponse(specialize);
    }
}
