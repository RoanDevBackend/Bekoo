package com.example.bookingserver.application.command.handle.chatbot;


import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.repository.UserRepository;
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
            return;
        }

        chatBotService.addUserChat(userId, content);

        // Check xem AI có khả năng trả lời hay không

        String botResponse = chatBotService.askAI(content, userId);
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // PARSE TO JSON
        String botMessage = String.format("{\"botResponse\": \"%s\", \"time\": \"%s\"}", botResponse, time);
        session.sendMessage(new TextMessage(botMessage));
    }

//    public void sendMessageToUser(String userId, String message) throws IOException {
//        WebSocketSession session = userSessions.get(userId);
//        if (session != null && session.isOpen()) {
//            session.sendMessage(new TextMessage(message));
//        }
//    }
}
