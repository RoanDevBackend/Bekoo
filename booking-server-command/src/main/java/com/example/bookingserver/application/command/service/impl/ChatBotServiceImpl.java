package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.reponse.ListUserChatResponse;
import com.example.bookingserver.application.command.reponse.UserResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.ChatMapper;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
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
    UserMapper userMapper;

    @Override
    public boolean checkUserIdExits(String id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        return chatBotRepository.existsBySenderId(id);
    }

    @Override
    public int takeGroupIdByUserId(String id) {
        if (chatBotRepository.findBySenderId(id).isEmpty()) {
            throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        Message userMessage = chatBotRepository.findBySenderId(id).get(0);
        return userMessage.getGroupId();
    }

    @Override
    public boolean addNewChat(String id, String content, boolean isUser) {
        saveContent(id, content, isUser, chatBotRepository.findMaxGroupId() + 1);
        return true;
    }

    @Override
    public boolean saveContent(String id, String content, boolean isUser, int groupId) {
        Message chatMessage = Message.builder()
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));

        int group;
        if (checkUserIdExits(id)) {
            group = takeGroupIdByUserId(id);
            saveContent(id, content, true, group);
        } else {
            addNewChat(id, content, true);
        }
        return true;
    }

    @Override
    public String askAI(String userMessage, String userId) throws IOException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }

        String botResponse = askAI(userMessage);
        int group;

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
        if (chatBotRepository.findByGroupId(groupId) == null) {
            throw new BookingCareException(ErrorDetail.ERR_GROUP_NOT_EXISTED);
        }
        List<ChatBotResponse> chatBotResponses = new ArrayList<>();

        List<Message> messages = chatBotRepository.findByGroupIdOrderByTimestampAsc(groupId);

        for (Message m : messages) {
            chatBotResponses.add(chatMapper.toResponse(m));
        }
        return chatBotResponses;
    }

    @Override
    public List<ListUserChatResponse> getListUserChat() {
        List<Object[]> results = chatBotRepository.findLatestSenderPerGroup();
        List<ListUserChatResponse> listUserChatResponses = new ArrayList<>();
        for (Object[] row : results) {
            int groupId = (int) row[0];
            String senderId = (String) row[1];
            String content = (String) row[2];
            Timestamp timestamp = (Timestamp) row[3];

            User user = userRepository.findById(senderId)
                            .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));

            UserResponse userResponse = userMapper.toResponse(user);

            listUserChatResponses.add(new ListUserChatResponse(userResponse, groupId, content, timestamp.toLocalDateTime()));
        }
        return listUserChatResponses;
    }

    static String URL = "";

    public String askAI(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = "{ \"question\": \"" + prompt + "\" }";

        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Request failed: " + response;
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            return json.getString("answer");
        }
    }
}
