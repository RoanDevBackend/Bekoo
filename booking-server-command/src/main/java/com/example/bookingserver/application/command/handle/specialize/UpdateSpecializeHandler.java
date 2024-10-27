package com.example.bookingserver.application.command.handle.specialize;

import com.example.bookingserver.application.command.command.specialize.UpdateSpecializeCommand;
import com.example.bookingserver.application.command.event.specialize.SpecializeEvent;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.SpecializeResponse;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.SpecializeMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
    OutboxEventRepository outboxEventRepository;
    SpecializeMapper specializeMapper;
    ObjectMapper objectMapper;
    MessageProducer messageProducer;
    String TOPIC="update-specialize";

    @SneakyThrows
    @Transactional
    public SpecializeResponse execute(UpdateSpecializeCommand command){
        Specialize specialize= specializeRepository.findById(command.getId()).orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_SPECIALIZE_NOT_EXISTED)
        );
        specializeMapper.updateInfo(specialize, command);

        specialize= specializeRepository.save(specialize);

        SpecializeEvent event= specializeMapper.toSpecializeEvent(specialize);
        String content= objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("UPDATE-SPECIALIZE")
                .aggregateId(specialize.getId())
                .aggregateType("Specialize")
                .content(content)
                .status(ApplicationConstant.EventStatus.PENDING)
                .build();

        try {
            messageProducer.sendMessage(TOPIC, content);
            outboxEvent.setStatus(ApplicationConstant.EventStatus.SEND);
        }catch (Exception e){
            log.error("SEND EVENT UPDATE SPECIALIZE FAILED: {}", TOPIC );
        }
        outboxEventRepository.save(outboxEvent);
        return specializeMapper.toResponse(specialize);
    }
}
