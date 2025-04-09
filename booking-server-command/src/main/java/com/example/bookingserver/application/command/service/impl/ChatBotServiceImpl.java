package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {
    ChatBotJpaRepository chatBotRepository;

    @Override
    public boolean checkUserIdExits(String id) {
        if (chatBotRepository.existsBySenderId(id)) return true;
        return false;
    }

    @Override
    public int takeGroupIdByUserId(String id) {
        Message userMessage = chatBotRepository.findBySenderId(id);
        return userMessage.getGroupId();
    }

    @Override
    public boolean addNewChat(String id, String content, boolean isUser) {
        if (isUser) saveContent(id, content, true, chatBotRepository.findMaxGroupId()+1);
        else {
            saveContent(id, content, false, chatBotRepository.findMaxGroupId());
        }
        return true;
    }

    @Override
    public boolean saveContent(String id, String content, boolean isUser, int groupId) {
        Message chatMessage = new Message().builder()
                .groupId(groupId)
                .content(content)
                .senderId((isUser) ? id : null)
                .timestamp(LocalDateTime.now())
                .build();
        chatBotRepository.save(chatMessage);
        return true;
    }
}
