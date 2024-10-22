package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.command.command.specialize.CreateSpecializeCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Department;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.DepartmentRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CreateSpecializeHandler {
    SpecializeRepository specializeRepository;
    DepartmentRepository departmentRepository;
    SpecializeMapper specializeMapper;

    @SneakyThrows
    public SpecializeResponse execute(CreateSpecializeCommand command){
        Department department= departmentRepository.findById(command.getDepartmentId()).orElseThrow(
                ()-> new BookingCareException(ErrorDetail.ERR_DEPARTMENT_NOT_EXISTED)
        );
        Specialize specialize= specializeMapper.toSpecialize(command);
        specialize.setDepartment(department);
        specializeRepository.save(specialize);
        log.info("CREATE SPECIALIZE SUCCESS: {}", command);
        return  specializeMapper.toResponse(specialize);
    }
}
