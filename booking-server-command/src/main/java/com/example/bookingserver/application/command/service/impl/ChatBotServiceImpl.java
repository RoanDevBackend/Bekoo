package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.reponse.*;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.application.command.service.MessageService;
import com.example.bookingserver.domain.Message;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.mapper.ChatMapper;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.persistence.repository.ChatBotJpaRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {
    final ChatBotJpaRepository chatBotRepository;
    final UserRepository userRepository;
    final ObjectMapper objectMapper;
    final RedisRepository redisRepository;

    @Value("${ai.url}")
    String URL;

    @Override
    public String chat(WebSocketSession adminSession, Map<String, String> data) {
        String senderId = data.get("senderId");
        User user = userRepository.findById(senderId == null ? "" : senderId).orElse(null);
        String content = data.get("content");
        String messageResponseFromAI = "";
        if(user == null){
            messageResponseFromAI = this.askAI(content, false);
        }
        if(user != null){
            messageResponseFromAI = this.askAI(content, true);

            System.out.println(messageResponseFromAI);
            int groupId = this.getGroupId(senderId);
            //save message
            Message messageUser = new Message();
            messageUser.setContent(content);
            messageUser.setSender(user);
            messageUser.setGroupId(groupId);
            chatBotRepository.save(messageUser);

            this.sendToAdmin(adminSession);

            if(!messageResponseFromAI.equals("null")){
                Message messageBot = new Message();
                messageBot.setSender(null);
                messageBot.setGroupId(groupId);
                messageBot.setContent(messageResponseFromAI);
                chatBotRepository.save(messageBot);
            }
        }
        return messageResponseFromAI;
    }

    @SneakyThrows
    private void sendToAdmin(WebSocketSession adminSession){
        if(adminSession != null){
            ApiResponse responseGetAllChat = ApiResponse.success(200, "Get-All-Chat", this.getAllChat(null));
            adminSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseGetAllChat)));
        }
    }

    private int getGroupId(String senderId){
        Integer groupId = chatBotRepository.findFirstBySenderId(senderId);
        return groupId == null ? chatBotRepository.findMaxGroupId() + 1 : groupId;
    }

    @Override
    public List<GetAllChatResponse> getAllChat(String name) {
        if (name == null) {
            name = "";
        }
        final String finalName = name;

        return chatBotRepository.getAllChat().stream().map(t -> {
            List<Message> messages = chatBotRepository.getMessageByGroupId(t.getGroupId(), true, PageRequest.of(0, 1)).getContent();
            if(!messages.isEmpty() && (finalName.isEmpty() || messages.get(0).getSender().getName().contains(finalName))){
                GetAllChatResponse getAllChatResponse = new GetAllChatResponse();
                getAllChatResponse.setContent(t.getContent());
                getAllChatResponse.setTime(this.convertDateToString(t.getCreatedAt()));
                getAllChatResponse.setName(messages.get(0).getSender().getName());
                getAllChatResponse.setUserId(messages.get(0).getSender().getId());
                getAllChatResponse.setSenderId(t.getSender() == null ? null : t.getSender().getId());
                getAllChatResponse.setUrlImage(messages.get(0).getSender().getLinkAvatar());
                getAllChatResponse.setOnline(this.getOnline(messages.get(0).getSender().getId()));
                return getAllChatResponse;
            }
            return null;
        }).filter(Objects::nonNull).toList();
    }

    private String getOnline(String userId){
        Object lastedOnline = redisRepository.get("WS" + userId);
        if(lastedOnline != null && lastedOnline.toString().equals("Online")){
            return "Online";
        }else{
            if(lastedOnline != null) {
                LocalDateTime timeOnline = LocalDateTime.parse(lastedOnline.toString());
                return this.convertDateToString(timeOnline);
            }else{
                return "Undefined";
            }
        }
    }

    private String convertDateToString(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(time, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 60) {
            return "Vài giây trước";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days <= 5) {
            return days + " ngày trước";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return time.format(formatter);
        }
    }

    @Override
    public List<ChatBotResponse> getChatHistory(String userId) {
        int groupId = this.getGroupId(userId);
        List<Message> messages = chatBotRepository.getMessageByGroupId(groupId, false,Pageable.unpaged()).getContent();
        return messages.stream().map(t-> {
            ChatBotResponse chatBotResponse = new ChatBotResponse();
            chatBotResponse.setContent(t.getContent());
            chatBotResponse.setCreatedBy(t.getSender() == null ? "Hệ thống" : "Người dùng");
            chatBotResponse.setCreatedAt(this.convertDateToString(t.getCreatedAt()));
            return chatBotResponse;
        }).toList();
    }


    @Override
    public void adminChat(Map<String, String> data) {
        String toUserId = data.get("toUserId");
        User user = userRepository.findById(toUserId == null ? "" : toUserId).orElse(null);
        String content = data.get("content");
        if(user != null){
            int groupId = this.getGroupId(toUserId);
            //save message
            Message messageAdmin = new Message();
            messageAdmin.setContent(content);
            messageAdmin.setSender(null);
            messageAdmin.setGroupId(groupId);

            chatBotRepository.save(messageAdmin);
        }
    }

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
//        Message chatMessage = Message.builder()
//                .groupId(groupId)
//                .content(content)
//                .senderId((isUser) ? id : null)
//                .timestamp(LocalDateTime.now())
//                .build();
//        chatBotRepository.save(chatMessage);
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
    public List<ChatBotResponse> getMessages(int groupId) {
        if (chatBotRepository.findByGroupId(groupId) == null) {
            throw new BookingCareException(ErrorDetail.ERR_GROUP_NOT_EXISTED);
        }
        List<ChatBotResponse> chatBotResponses = new ArrayList<>();

//        List<Message> messages = chatBotRepository.findByGroupIdOrderByTimestampAsc(groupId);

//        for (Message m : messages) {
//            chatBotResponses.add(chatMapper.toResponse(m));
//        }
        return chatBotResponses;
    }



    @SneakyThrows
    private String askAI(String prompt, boolean isLogin){
        OkHttpClient client = new OkHttpClient();
        String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);
        String fullUrl = URL + "?text=" + encodedPrompt + "&isloggedIn=" + isLogin;
        Request request = new Request.Builder()
                .url(fullUrl)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Request failed: " + response;
            }

            String responseBody = Objects.requireNonNull(response.body()).string();
            return objectMapper.readValue(responseBody, Map.class).get("response") + "";
        }
    }
}
