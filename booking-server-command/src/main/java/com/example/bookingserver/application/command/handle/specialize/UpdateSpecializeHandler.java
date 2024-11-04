package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.command.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.specialize.SpecializeEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UpdateSpecializeHandler {
    SpecializeRepository specializeRepository;
    SpecializeMapper specializeMapper;
    MessageProducer messageProducer;
    String TOPIC= TopicConstant.SpecializeTopic.UPDATE_SPECIALIZE;

    @SneakyThrows
    @Transactional
    public SpecializeResponse execute(UpdateSpecializeCommand command){
        Specialize specialize= specializeRepository.findById(command.getId()).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED)
        );
        specializeMapper.updateInfo(specialize, command);

        specialize= specializeRepository.save(specialize);

        SpecializeEvent event= specializeMapper.toSpecializeEvent(specialize);
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.UPDATE, event, event.getId(), "Specialize");
        return specializeMapper.toResponse(specialize);
    }
}
