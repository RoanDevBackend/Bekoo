package com.example.bookingserver.application.handle.doctor_department;

import com.example.bookingserver.application.command.doctor_department.DeleteDoctorDepartmentCommand;
import com.example.bookingserver.application.event.doctor_department.DoctorDepartmentEvent;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.domain.DoctorDepartment;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorDepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorRepository;
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
public class DeleteDoctorDepartmentHandler implements Handler<DeleteDoctorDepartmentCommand> {


    final DoctorDepartmentRepository doctorDepartmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "delete-doctor-department-event";

    @Override
    @SneakyThrows
    public void execute(DeleteDoctorDepartmentCommand command) {
        List<DeleteDoctorDepartmentCommand.Value> ids= command.getValues();

        for(DeleteDoctorDepartmentCommand.Value x: ids){

            var doctorDepartment= doctorDepartmentRepository.findById(x.getDoctorId(), x.getDepartmentId());

            if(doctorDepartment.isPresent()) {
                DoctorDepartmentEvent doctorDepartmentEvent= new DoctorDepartmentEvent(x.getDoctorId(), x.getDepartmentId());
                String content = objectMapper.writeValueAsString(doctorDepartmentEvent);
                doctorDepartmentRepository.delete(doctorDepartment.get());
                OutboxEvent outboxEvent = OutboxEvent.builder()
                        .topic(TOPIC)
                        .eventType("DELETE")
                        .aggregateId(content)
                        .aggregateType("doctor department")
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
