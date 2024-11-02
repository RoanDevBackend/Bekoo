package com.example.bookingserver.application.command.handle.department;


import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
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
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.DepartmentTopic.DELETE_DEPARTMENT;

    @Override
    @SneakyThrows
    public void execute(List<String> ids) {
        for(String x: ids){
            log.info("DELETE DEPARTMENT: {}", x);
            var department= departmentRepository.findById(x);

            if(department.isPresent()) {
                messageProducer.sendMessage(TOPIC, "DELETE", x, x, "Department");
            }
        }
    }
}
