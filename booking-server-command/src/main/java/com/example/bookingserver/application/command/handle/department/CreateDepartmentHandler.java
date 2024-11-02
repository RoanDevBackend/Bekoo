package com.example.bookingserver.application.command.handle.department;

import com.cloudinary.Cloudinary;
import com.example.bookingserver.application.command.command.department.CreateDepartmentCommand;
import com.example.bookingserver.application.command.handle.Handler_DTO;
import com.example.bookingserver.application.command.reponse.DepartmentResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.department.CreateDepartmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateDepartmentHandler implements Handler_DTO<CreateDepartmentCommand, DepartmentResponse> {

    final DepartmentRepository departmentRepository;
    final DepartmentMapper departmentMapper;
    final MessageProducer messageProducer;
    final Cloudinary cloudinary;
    final String TOPIC= TopicConstant.DepartmentTopic.CREATE_DEPARTMENT;

    @Override
    public DepartmentResponse execute(CreateDepartmentCommand command) {
        String url_data;
        try {
            Map responseByCloudinary = cloudinary.uploader().upload(command.getImage().getBytes(), Map.of());
            url_data = responseByCloudinary.get("url") + "";
        }catch (Exception e){
            throw new RuntimeException("Hiện không thể lưu ảnh");
        }

        Department department= departmentMapper.toDepartment(command);
        department.setUrlImage(url_data);

        departmentRepository.save(department);
        CreateDepartmentEvent event= departmentMapper.toCreateDepartmentEvent(department);

        messageProducer.sendMessage(TOPIC, "CREATE", event, event.getId(), "Department");
        return departmentMapper.toResponse(department);
    }
}
