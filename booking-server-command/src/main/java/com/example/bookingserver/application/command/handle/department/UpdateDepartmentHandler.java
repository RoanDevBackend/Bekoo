package com.example.bookingserver.application.command.handle.department;

import com.cloudinary.Cloudinary;
import com.example.bookingserver.application.command.command.department.UpdateInfoDepartmentCommand;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.DepartmentResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.infrastructure.mapper.DepartmentMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.department.UpdateInfoDepartmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateDepartmentHandler implements HandlerDTO<UpdateInfoDepartmentCommand, DepartmentResponse> {
    final DepartmentRepository departmentRepository;
    final DepartmentMapper departmentMapper;
    final Cloudinary cloudinary;
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.DepartmentTopic.UPDATE_DEPARTMENT;

    @Override
    @SneakyThrows
    public DepartmentResponse execute(UpdateInfoDepartmentCommand command) {
        Department department= departmentRepository.findById(command.getId())
                        .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED));
        departmentMapper.updateInfo(department, command);

        if(command.getImage() != null){
            String url_data;
            try {
                Map responseByCloudinary = cloudinary.uploader().upload(command.getImage().getBytes(), Map.of());
                url_data = responseByCloudinary.get("url") + "";
                department.setUrlImage(url_data);
            }catch (Exception e){
                throw new RuntimeException("Hiện không thể lưu ảnh");
            }
        }
        departmentRepository.save(department);

        UpdateInfoDepartmentEvent event= departmentMapper.toUpdateDepartmentEvent(department);

        messageProducer.sendMessage(TOPIC, "UPDATE", event, event.getId(), "Department");
        return departmentMapper.toResponse(department);
    }
}
