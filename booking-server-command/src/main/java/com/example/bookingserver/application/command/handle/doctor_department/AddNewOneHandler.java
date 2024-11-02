package com.example.bookingserver.application.command.handle.doctor_department;


import com.example.bookingserver.application.command.command.doctor_department.AddNewOneCommand;
import com.example.bookingserver.application.command.event.doctor_department.DoctorDepartmentEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Doctor;
import com.example.bookingserver.domain.DoctorDepartment;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorDepartmentRepository;
import com.example.bookingserver.domain.repository.DoctorRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
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
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.DoctorDepartmentTopic.ADD_NEW_ONE;

    @Override
    @SneakyThrows
    @Transactional()
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
        messageProducer.sendMessage(TOPIC, "CREATE", event, event.getDoctorId() + " " + event.getDepartmentId(), "Doctor-Department");
    }
}
