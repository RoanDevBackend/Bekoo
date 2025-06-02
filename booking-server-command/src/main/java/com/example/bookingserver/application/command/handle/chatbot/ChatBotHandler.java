package com.example.bookingserver.application.command.handle.chatbot;


import com.example.bookingserver.application.command.command.chatbot.ChatMessageCommand;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotHandler extends TextWebSocketHandler {
    ChatBotService chatBotService;
    ObjectMapper objectMapper;
    UserRepository userRepository;
    RedisRepository redisRepository;

    static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserIdFromQuery(Objects.requireNonNull(session.getUri()).getQuery());
        if (userId != null) {
            sessionMap.put(userId, session);
            redisRepository.set("WS" + userId, "Online"); //Đang hoạt động
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
        String userId = extractUserIdFromQuery(Objects.requireNonNull(session.getUri()).getQuery());
        if (userId != null) {
            sessionMap.remove(userId);
            redisRepository.set("WS" + userId, LocalDateTime.now().toString()); //Thời gian gần nhất hoạt động
        }
    }

    private String extractUserIdFromQuery(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("userId")) {
                return pair[1];
            }
        }
        return null;
    }

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage TextMessage) throws IOException {
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
            String errMsg = this.validateData(command);
            if(!errMsg.isBlank()){
                ApiResponse apiResponse = ApiResponse.error(400, errMsg);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(apiResponse)));
                return;
            }
            String responseFromAI = chatBotService.chat(getAdminSession(), command.getData());
            if(!responseFromAI.equals("null")){
                ApiResponse response = ApiResponse.success(200, "Chat", responseFromAI);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response))); // Gửi dữ liệu tin nhắn về cho client
                this.sendToAdmin();
            }else{
                ApiResponse response = ApiResponse.success(400, "Chat", "");
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response))); // Gửi dữ liệu tin nhắn về cho client
            }
        }else if(command.getRequestType().equals("Get-All-Chat")){
            ApiResponse response = ApiResponse.success(200, "Get-All-Chat", chatBotService.getAllChat(command.getData().get("name")));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));

        }else if(command.getRequestType().equals("Get-Chat-History")){
            String userId = command.getData().get("userId");
            ApiResponse response = ApiResponse.success(200, "Get-Chat-History", chatBotService.getChatHistory(userId));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));

        }else if(command.getRequestType().equals("Admin-Chat")){
            String errMsg = this.validateData(command);
            if(!errMsg.isBlank()){
                ApiResponse apiResponse = ApiResponse.error(400, errMsg);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(apiResponse)));
                return;
            }
            chatBotService.adminChat(command.getData());
            ApiResponse response = ApiResponse.success(200, "Admin-Chat");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));

            User userClient = userRepository.findById(command.getData().get("toUserId")).orElse(null);
            if(userClient != null){
                WebSocketSession sessionAdmin = sessionMap.get(userClient.getId());
                if(sessionAdmin != null){
                    ApiResponse responseGetChatHistory = ApiResponse.success(200, "Get-Chat-History", chatBotService.getChatHistory(userClient.getId()));
                    sessionAdmin.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseGetChatHistory)));
                }
            }
        }
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    public void updateOnline(){
        this.sendToAdmin();
    }

    @SneakyThrows
    private void sendToAdmin(){
        WebSocketSession adminSession = this.getAdminSession();
        if(adminSession != null){
            ApiResponse responseGetAllChat = ApiResponse.success(200, "Get-All-Chat", chatBotService.getAllChat(null));
            adminSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseGetAllChat)));
        }
    }

    private WebSocketSession getAdminSession(){
        User userAdmin = userRepository.findByUserName("bekoo.admin@gmail.com");
        if(userAdmin != null) {
            return sessionMap.get(userAdmin.getId());
        }
        return null;
    }

    private String validateData(ChatMessageCommand command){
        if(command.getData() == null){
            return "Data không được null";
        }else{
            if(command.getData().get("content") == null || command.getData().get("content").isBlank()){
                return "Content không được bỏ trống";
            }
        }
        return "";
    }

}
