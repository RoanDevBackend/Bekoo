package com.example.bookingserver.application.command.service;

import com.example.bookingserver.domain.ChatMessage;

public interface ChatMessageService {
    ChatMessage saveMessage(String content, String sender);
    String getAIResponse(String userMessage);
}
