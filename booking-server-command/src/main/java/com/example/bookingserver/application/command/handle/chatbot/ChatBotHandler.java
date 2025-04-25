package com.example.bookingserver.application.command.handle.chatbot;


import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotHandler extends TextWebSocketHandler {
    ChatBotService chatBotService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        JSONObject json = new JSONObject(message.getPayload());
        String userId = json.getString("senderId");
        String content = json.getString("content");
//        String adminStatus = json.getString("admin_status");

        System.out.println("Receive content: " + content);

        if (userId.equals("751e02a8-658f-4ef3-a415-2089ff0819b0")){
            chatBotService.saveContent(null, content, false, chatBotService.takeGroupIdByUserId(userId));
            String response = String.format("{\"userId\": \"%s\", \"content\": \"%s\"}", userId, "");
            session.sendMessage(new TextMessage(response));
            System.out.println("ADMIN_ON");
            return;
        }

        // GET HISTORY OF MESSAGE
        if (content.equals("GET MESSAGE")){
            session.sendMessage(new TextMessage(getHistoryMessage(userId)));
            System.out.println("GET HISTORY MESSAGE SUCCESS");
            return;
        }

        chatBotService.addUserChat(userId, content);

        // Check xem AI có khả năng trả lời hay không

        session.sendMessage(new TextMessage(botResponse(content, userId)));
    }


    // Phản hồi từ AI
    private String botResponse(String content, String userId) throws IOException {
        String botMessage = chatBotService.askAI(content, userId);
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // PARSE TO JSON
        String safeBotResponse = botMessage.replace("\"", "\\\"");
        String message = String.format("{\"userId\": \"%s\", \"botResponse\": \"%s\", \"time\": \"%s\"}",userId, safeBotResponse, time);
        System.out.println("Bot response: " + botMessage);
        return message;
    }

    private String getHistoryMessage(String userId) throws JsonProcessingException {
        List<ChatBotResponse> responses = chatBotService.getMessages(chatBotService.takeGroupIdByUserId(userId));

        Map<String, Object> response = new HashMap<>();

        for(ChatBotResponse c : responses){
            response.put("content", c.getContent());
            response.put("type", c.getType());
            response.put("time", c.getTimestamp());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Convert list to JSON string
        String jsons = objectMapper.writeValueAsString(response);
        return jsons;
    }

}
