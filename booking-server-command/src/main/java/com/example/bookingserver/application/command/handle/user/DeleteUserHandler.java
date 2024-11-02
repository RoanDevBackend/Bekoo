package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.event.user.DeleteUserEvent;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.domain.OutboxEvent;
import com.example.bookingserver.domain.repository.OutboxEventRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import document.constant.TopicConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteUserHandler implements Handler<List<String>> {

    final UserRepository userRepository;
    final MessageProducer messageProducer;
    final ObjectMapper objectMapper;
    final OutboxEventRepository outboxEventRepository;
    final String TOPIC= TopicConstant.UserTopic.DELETE_USER;

    @SneakyThrows
    @Override
    public void execute(List<String> ids) {

        for(String id: ids){
            userRepository.delete(id);
        }

        DeleteUserEvent event= DeleteUserEvent.builder()
                .ids(ids)
                .build();
        String content= objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent= OutboxEvent.builder()
                .topic(TOPIC)
                .eventType("DELETE")
                .aggregateId("null")
                .aggregateType("User")
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
