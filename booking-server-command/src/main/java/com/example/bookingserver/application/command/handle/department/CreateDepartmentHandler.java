package com.example.bookingserver.application.command.handle.department;

import com.example.bookingserver.application.command.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.event.department.CreateDepartmentEvent;
import com.example.bookingserver.application.command.handle.Handler_DTO;
import com.example.bookingserver.application.command.reponse.DepartmentResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateDepartmentHandler implements Handler_DTO<CreateDepartmentCommand, DepartmentResponse> {

    final DepartmentRepository departmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final DepartmentMapper departmentMapper;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "create-department-event";

    @Override
    @SneakyThrows
    public DepartmentResponse execute(CreateDepartmentCommand command) {
        Department department= departmentMapper.toDepartment(command);
        departmentRepository.save(department);

        CreateDepartmentEvent event= departmentMapper.toCreateDepartmentEvent(department);

        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("CREATE")
                .aggregateId(department.getId())
                .aggregateType("Department")
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
        return departmentMapper.toResponse(department);
    }
}
