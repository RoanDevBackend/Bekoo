package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.user.DeleteUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteUserHandler implements Handler<List<String>> {

    final UserRepository userRepository;
    final MessageProducer messageProducer;
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
        messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.DELETE, event, null, "User" );
    }
}
