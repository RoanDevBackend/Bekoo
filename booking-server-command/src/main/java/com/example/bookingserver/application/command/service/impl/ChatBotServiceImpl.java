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
    public boolean checkUserIdExits(Long id) {
        if (chatBotRepository.existsBySenderId(id)) return false;
        return true;
    }

    @Override
    public int takeGroupIdByUserId(Long id) {
        Message userMessage = chatBotRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return userMessage.getGroupId();
    }

    @Override
    public boolean addNewUser(Long id, String content) {
        if (!checkUserIdExits(id))
            return false;
        if (saveContent(id, content, true, 1)) return true;
        return false;
    }

    @Override
    public boolean saveContent(Long id, String content, boolean isUser, int groupId) {
        Message chatMessage = new Message().builder()
                .groupId(groupId)
                .content(content)
                .sender_id((isUser) ? id : null)
                .timestamp(LocalDateTime.now())
                .build();
        chatBotRepository.save(chatMessage);
        return true;
    }
}
