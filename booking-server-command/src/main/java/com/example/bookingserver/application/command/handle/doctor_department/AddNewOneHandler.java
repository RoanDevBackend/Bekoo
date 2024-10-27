package com.example.bookingserver.application.command.handle.doctor_department;


import com.example.bookingserver.application.command.command.doctor_department.AddNewOneCommand;
import com.example.bookingserver.application.command.event.doctor_department.DoctorDepartmentEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.DoctorDepartment;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorDepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddNewOneHandler implements Handler<AddNewOneCommand> {

    final DoctorDepartmentRepository doctorDepartmentRepository;
    final DoctorRepository doctorRepository;
    final DepartmentRepository departmentRepository;
    final OutboxEventRepository outboxEventRepository;
    final ObjectMapper objectMapper;
    final MessageProducer messageProducer;
    final String TOPIC= "add-new-one-doctor-department-event";

    @Override
    @SneakyThrows
    @Transactional
    public void execute(AddNewOneCommand command) {
        Doctor doctor= doctorRepository.findById(command.getDoctorId())
                        .orElseThrow(
                                () -> new BookingCareException(ErrorDetail.ERR_DOCTOR_NOT_EXISTED)
                        );

        Department department= departmentRepository.findById(command.getDepartmentId())
                        .orElseThrow(
                                () -> new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED)
                        );

        var isExisted= doctorDepartmentRepository.findById(command.getDoctorId(), command.getDepartmentId());
        if(isExisted.isPresent()){
            throw new RuntimeException("Đã tồn tại");
        }

        DoctorDepartment doctorDepartment= new DoctorDepartment(doctor, department);
        doctorDepartmentRepository.save(doctorDepartment);

        DoctorDepartmentEvent event= new DoctorDepartmentEvent(command.getDoctorId(), command.getDepartmentId());

        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("CREATE")
                .aggregateId("Doctor - Department ID")
                .aggregateType("Doctor- Department")
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
    }
}
