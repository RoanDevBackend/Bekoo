package com.example.bookingserver.application.command.handle.doctor_department;

import com.example.bookingserver.application.command.command.doctor_department.DeleteDoctorDepartmentCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.domain.repository.DoctorDepartmentRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.doctor_department.DoctorDepartmentEvent;
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
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.DoctorDepartmentTopic.DELETE;

    @Override
    @SneakyThrows
    public void execute(DeleteDoctorDepartmentCommand command) {
        List<DeleteDoctorDepartmentCommand.Value> ids= command.getValues();

        for(DeleteDoctorDepartmentCommand.Value x: ids){

            var doctorDepartment= doctorDepartmentRepository.findById(x.getDoctorId(), x.getDepartmentId());

            if(doctorDepartment.isPresent()) {
                DoctorDepartmentEvent doctorDepartmentEvent= new DoctorDepartmentEvent(x.getDoctorId(), x.getDepartmentId());
                messageProducer.sendMessage(TOPIC, "DELETE", doctorDepartmentEvent, doctorDepartmentEvent.getDoctorId() + " " + doctorDepartmentEvent.getDepartmentId(), "Doctor Department");
            }
        }
    }
}
