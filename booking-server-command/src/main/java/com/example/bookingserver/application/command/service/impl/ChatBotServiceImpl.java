package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.ChatMapper;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {
    ChatBotJpaRepository chatBotRepository;
    UserRepository userRepository;
    ChatMapper chatMapper;

    @Override
    public boolean checkUserIdExits(String id) {
        if(userRepository.findById(id).isEmpty()){
            throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        return chatBotRepository.existsBySenderId(id);
    }

    @Override
    public int takeGroupIdByUserId(String id) {
        Message userMessage = chatBotRepository.findBySenderId(id).get(0);
        return userMessage.getGroupId();
    }

    @Override
    public boolean addNewChat(String id, String content, boolean isUser) {
        if (isUser) saveContent(id, content, true, chatBotRepository.findMaxGroupId() + 1);
        else {
            saveContent(id, content, false, chatBotRepository.findMaxGroupId());
        }
        return true;
    }

    @Override
    public boolean saveContent(String id, String content, boolean isUser, int groupId) {
        Message chatMessage = new Message().builder()
                .groupId(groupId)
                .content(content)
                .senderId((isUser) ? id : null)
                .timestamp(LocalDateTime.now())
                .build();
        chatBotRepository.save(chatMessage);
        return true;
    }

    @Override
    public boolean addUserChat(String id, String content) {
        int group = 0;
        if (!content.equals("") && !id.equals("")) {
            if (checkUserIdExits(id)) {
                group = takeGroupIdByUserId(id);
                saveContent(id, content, true, group);
            } else {
                addNewChat(id, content, true);
            }
        }
        return true;
    }

    @Override
    public String askAI(String userMessage, String userId) throws IOException {
        String botResponse = askGemini(userMessage);
        int group = 0;

        if (checkUserIdExits(userId)) {
            group = takeGroupIdByUserId(userId);
            saveContent(userId, botResponse, false, group);
        } else {
            addNewChat(userId, botResponse, false);
        }
        return botResponse;
    }

    @Override
    public List<ChatBotResponse> getMessages(int groupId) {
        if (chatBotRepository.findByGroupId(groupId).isEmpty()){
            throw new RuntimeException("Không lấy được dữ liệu đoạn chat");
        }
        List<ChatBotResponse> chatBotResponses = new ArrayList<>();
        List<Message> messages = chatBotRepository.findByGroupId(groupId);
        for(Message m : messages){
            chatBotResponses.add(chatMapper.toResponse(m));
        }
        return chatBotResponses;
    }

    static String API_KEY = "AIzaSyC53a7Hl-WqvIX2nJQr77JIA5ZeLtTwAZ4";
    static String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
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
