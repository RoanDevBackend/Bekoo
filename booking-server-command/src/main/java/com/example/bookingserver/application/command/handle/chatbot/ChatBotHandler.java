package com.example.bookingserver.application.command.handle.chatbot;


import com.example.bookingserver.application.command.command.chatbot.ChatMessageCommand;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotHandler extends TextWebSocketHandler {
    ChatBotService chatBotService;
    ObjectMapper objectMapper;
    UserRepository userRepository;
    ChatBotJpaRepository chatBotJpaRepository;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage TextMessage) throws IOException {
        String payload = TextMessage.getPayload();
        ChatMessageCommand command;
        try {
            command = objectMapper.readValue(payload, ChatMessageCommand.class);
        } catch (JsonProcessingException e) {
            ApiResponse apiResponse = ApiResponse.error(400, "Có lỗi trong quá trình chuyển đổi dữ liệu từ Client");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(apiResponse)));
            return;
        }
        if(command.getRequestType().equals("Chat")){
            String responseFromAI = chatBotService.chat(command.getData());
            ApiResponse response = ApiResponse.success(200, "Success", responseFromAI);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }else if(command.getRequestType().equals("Get-All-Chat")){
            ApiResponse response = ApiResponse.success(200, "Success", chatBotService.getAllChat(command.getData().get("name")));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }else if(command.getRequestType().equals("Get-Chat-History")){
            String userId = command.getData().get("userId");
            ApiResponse response = ApiResponse.success(200, "Success", chatBotService.getChatHistory(userId));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }
    }

}
