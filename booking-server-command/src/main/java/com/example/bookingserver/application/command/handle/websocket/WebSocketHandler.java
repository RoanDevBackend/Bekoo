package com.example.bookingserver.application.command.handle.websocket;

import com.example.bookingserver.application.command.service.impl.ChatBotServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    static String API_KEY = "AIzaSyC53a7Hl-WqvIX2nJQr77JIA5ZeLtTwAZ4";
    static String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    ChatBotServiceImpl chatBotService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userMessage = message.getPayload();
        String botResponse = callChatBotAPI(userMessage);

        // demo admin
        String userId = "751e02a8-658f-4ef3-a415-2089ff0819b0";
        int group = 0;

        if (chatBotService.checkUserIdExits(userId)) {
            group = chatBotService.takeGroupIdByUserId(userId);
            chatBotService.saveContent(userId, userMessage, true, group);
            chatBotService.saveContent(userId, botResponse, false, group);
        } else {
            chatBotService.addNewChat(userId, userMessage, true);
            chatBotService.addNewChat(userId, botResponse, false);
        }

        // Gửi lại phản hồi cho người dùng
        session.sendMessage(new TextMessage(botResponse));
    }

    private String callChatBotAPI(String message) throws IOException {
        return askGemini(message);
    }

    public String askGemini(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        Request request = new Request.Builder()
                .url(GEMINI_URL)
                .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Request failed: " + response;
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            return jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .replace("*", "");
        }
    }
}
