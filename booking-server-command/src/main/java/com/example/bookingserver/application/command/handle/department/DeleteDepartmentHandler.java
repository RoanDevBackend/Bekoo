package com.example.bookingserver.application.command.handle.department;


import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
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
public class DeleteDepartmentHandler implements Handler<List<String>> {

    final DepartmentRepository departmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "delete-department-event";

    @Override
    @SneakyThrows
    public void execute(List<String> ids) {

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
