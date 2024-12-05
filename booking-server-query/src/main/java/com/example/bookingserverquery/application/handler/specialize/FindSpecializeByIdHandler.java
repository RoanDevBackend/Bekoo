package com.example.bookingserverquery.application.handler.specialize;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.domain.Specialize;
import com.example.bookingserverquery.infrastructure.repository.SpecializeELRepository;
import document.response.SpecializeResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindSpecializeByIdHandler {

    final SpecializeELRepository specializeELRepository;
    final SpecializeUtils specializeUtils;

    @SneakyThrows
    public SpecializeResponse execute(String id) {
        Specialize specialize= specializeELRepository.findById(id).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED)
        );
        return specializeUtils.convertToResponse(specialize);
    }
}
