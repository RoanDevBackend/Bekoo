package com.example.bookingserver.application.handle.department;


import com.example.bookingserver.application.command.department.DeleteDepartmentCommand;
import com.example.bookingserver.application.handle.Handler;
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

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteDepartmentHandler implements Handler<DeleteDepartmentCommand> {

    final DepartmentRepository departmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final DepartmentMapper departmentMapper;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "delete-department-event";

    @Override
    @SneakyThrows
    public void execute(DeleteDepartmentCommand command) {
        List<String> ids= command.getIds();

        for(String x: ids){
            log.info("DELETE DEPARTMENT: {}", x);

            var department= departmentRepository.findById(x);

            if(department.isPresent()) {
                String content = objectMapper.writeValueAsString(x);
                departmentRepository.delete(department.get());
                OutboxEvent outboxEvent = OutboxEvent.builder()
                        .topic(TOPIC)
                        .eventType("DELETE")
                        .aggregateId(content)
                        .aggregateType("Department")
                        .content(content)
                        .status(ApplicationConstant.EventStatus.PENDING)
                        .build();

                try {
                    messageProducer.sendMessage(TOPIC, content);
                    outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
                    log.info("SEND EVENT SUCCESS: {}", TOPIC);
                } catch (Exception e) {
                    log.error("SEND EVENT FAILED: {}", TOPIC);
                }
                outboxEventRepository.save(outboxEvent);
            }
        }
    }
}
