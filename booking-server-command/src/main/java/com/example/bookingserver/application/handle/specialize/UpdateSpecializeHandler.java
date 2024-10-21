package com.example.bookingserver.application.handle.specialize;

import com.example.bookingserver.application.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.reponse.SpecializeResponse;
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
public class UpdateSpecializeHandler {
    SpecializeRepository specializeRepository;
    SpecializeMapper specializeMapper;

    @SneakyThrows
    public SpecializeResponse execute(UpdateSpecializeCommand command){
        Specialize specialize= specializeRepository.findById(command.getId()).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED)
        );
        specializeMapper.updateInfo(specialize, command);
        specializeRepository.save(specialize);
        return specializeMapper.toResponse(specialize);
    }
}
