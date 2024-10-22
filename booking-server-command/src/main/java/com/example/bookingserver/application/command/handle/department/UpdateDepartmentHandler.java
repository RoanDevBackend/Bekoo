package com.example.bookingserver.application.command.handle.department;

import com.example.bookingserver.application.command.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.command.event.department.UpdateInfoDepartmentEvent;
import com.example.bookingserver.application.command.handle.Handler_DTO;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
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
public class UpdateDepartmentHandler implements Handler_DTO<UpdateInfoDepartmentCommand, DepartmentResponse> {
    final DepartmentRepository departmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final DepartmentMapper departmentMapper;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "update-info-department-event";

    @Override
    @SneakyThrows
    public DepartmentResponse execute(UpdateInfoDepartmentCommand command) {
        Department department= departmentRepository.findById(command.getId())
                        .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED));
        departmentMapper.updateInfo(department, command);

        departmentRepository.save(department);

        UpdateInfoDepartmentEvent event= departmentMapper.toUpdateDepartmentEvent(department);

        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("UPDATE")
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
