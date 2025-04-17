package com.example.bookingserver.application.command.handle.chatbot;


import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        if (userId.equals("751e02a8-658f-4ef3-a415-2089ff0819b0")){
            chatBotService.saveContent(null, content, false, chatBotService.takeGroupIdByUserId(userId));
            session.sendMessage(new TextMessage(""));
            return;
        }

        if (content.equals("GET MESSAGE")){

            if (!chatBotService.checkUserIdExits(userId)) {
                ChatBotResponse chatBotResponse = new ChatBotResponse("Xin chào! Tôi có thể giúp gì cho bạn?", 0, LocalDateTime.now());
                String safeContent = chatBotResponse.getContent().replace("\"", "\\\"");
                String time = chatBotResponse.getTimestamp().toString();
                int type = chatBotResponse.getType();
                String newChatResponse = String.format(
                        "{\"botResponse\": \"%s\", \"time\": \"%s\", \"type\": %d}",
                        safeContent, time, type
                );
                session.sendMessage(new TextMessage(newChatResponse));
                System.out.println("Lần đầu sử dụng");
                return;
            }

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
            session.sendMessage(new TextMessage(jsons));
            System.out.println("Nhận message thành công");
            return;
        }

        chatBotService.addUserChat(userId, content);

        // Check xem AI có khả năng trả lời hay không

        String botResponse = chatBotService.askAI(content, userId);
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // PARSE TO JSON
        String safeBotResponse = botResponse.replace("\"", "\\\""); // escape dấu "
        String botMessage = String.format("{\"botResponse\": \"%s\", \"time\": \"%s\"}", safeBotResponse, time);
        session.sendMessage(new TextMessage(botMessage));
        System.out.println(content);
    }

//    public void sendMessageToUser(String userId, String message) throws IOException {
//        WebSocketSession session = userSessions.get(userId);
//        if (session != null && session.isOpen()) {
//            session.sendMessage(new TextMessage(message));
//        }
//    }
}
