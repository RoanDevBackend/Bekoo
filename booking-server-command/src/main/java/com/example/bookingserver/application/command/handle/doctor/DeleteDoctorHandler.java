package com.example.bookingserver.application.command.handle.doctor;

import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.user.DeleteUserHandler;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteDoctorHandler implements Handler<List<String>> {

    final DoctorRepository doctorRepository;
    final OutboxEventRepository outboxEventRepository;
    final MessageProducer messageProducer;
    final ObjectMapper objectMapper;
    final DeleteUserHandler deleteUserHandler;
    final String TOPIC="delete-doctor-event";

    @Override
    @SneakyThrows
    @Transactional
    public void execute(List<String> ids) {
        for(String x: ids){
            log.info("DELETE DOCTOR: {}", x);

            var doctor= doctorRepository.findById(x);

            List<String> idsUser= new ArrayList<>();
            if(doctor.isPresent()) {
                String content = objectMapper.writeValueAsString(x);
                doctorRepository.delete(doctor.get());
                idsUser.add(doctor.get().getUser().getId());
                OutboxEvent outboxEvent = OutboxEvent.builder()
                        .topic(TOPIC)
                        .eventType("DELETE")
                        .aggregateId(content)
                        .aggregateType("Doctor")
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
            deleteUserHandler.execute(idsUser);
        }
    }

}
