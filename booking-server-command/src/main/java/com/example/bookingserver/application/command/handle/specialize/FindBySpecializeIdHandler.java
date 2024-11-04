package com.example.bookingserver.application.command.handle.specialize;


import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class FindBySpecializeIdHandler {
    SpecializeRepository specializeRepository;
    SpecializeMapper specializeMapper;

    @SneakyThrows
    public SpecializeResponse execute(String id){
        var specialize= specializeRepository.findById(id).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED)
        );
        return specializeMapper.toResponse(specialize);
    }
}
