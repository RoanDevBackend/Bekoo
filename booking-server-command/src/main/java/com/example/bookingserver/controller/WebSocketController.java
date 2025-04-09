package com.example.bookingserver.controller;

import com.example.bookingserver.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

//@Controller
//@RequestMapping("/ws")
//public class WebSocketController {
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
//}

public class WebSocketController extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userMessage = message.getPayload();

        // Gọi API chatbot — đơn giản hóa thành một hàm mô phỏng
        String botResponse = callChatBotAPI(userMessage);

        // Gửi lại phản hồi cho người dùng
        session.sendMessage(new TextMessage(botResponse));
    }

    // Hàm mô phỏng gọi API chatbot (bạn thay bằng thật nhé)
    private String callChatBotAPI(String message) {
        // Giả sử bạn có API KEY và API URL (tạm trả về chuỗi cứng)
        return "Bot trả lời: Bạn vừa nói '" + message + "'";
    }
}
