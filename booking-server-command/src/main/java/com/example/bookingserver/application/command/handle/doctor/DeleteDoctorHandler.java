package com.example.bookingserver.application.command.handle.doctor;

import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.user.DeleteUserHandler;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
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
    final MessageProducer messageProducer;
    final DeleteUserHandler deleteUserHandler;
    final String TOPIC= TopicConstant.DoctorTopic.DELETE;

    @Override
    @SneakyThrows
    @Transactional
    public void execute(List<String> ids) {
        for(String x: ids){
            log.info("DELETE DOCTOR: {}", x);
            var doctor= doctorRepository.findById(x);
            List<String> idsUser= new ArrayList<>();
            if(doctor.isPresent()) {
                messageProducer.sendMessage(TOPIC, "DELETE", x, x, "Doctor");
            }
            deleteUserHandler.execute(idsUser);
        }
    }
}
