package com.example.bookingserver.application.command.handle.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

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
