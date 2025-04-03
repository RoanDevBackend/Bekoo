package com.example.bookingserver.application.command.handle.websocket;

import com.example.bookingserver.application.command.service.impl.ChatMessageServiceImpl;
import com.example.bookingserver.domain.ChatMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ChatMessageServiceImpl chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userMessage = message.getPayload();
        // Lưu tin nhắn người dùng vào DB
        ChatMessage savedUserMessage = chatService.saveMessage(userMessage, "USER");

        // Gọi API AI và lấy phản hồi
        String aiResponse = chatService.getAIResponse(userMessage);

        // Lưu phản hồi AI vào DB
        ChatMessage savedAIResponse = chatService.saveMessage(aiResponse, "AI");

        // Gửi lại cả tin nhắn người dùng và phản hồi AI cho client
        session.sendMessage(new TextMessage(savedUserMessage.getContent()));
        session.sendMessage(new TextMessage(savedAIResponse.getContent()));
    }
}