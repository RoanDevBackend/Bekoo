package com.example.bookingserver.application.command.service;

import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.reponse.GetAllChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ChatBotService {

    /**
     *
     * @param data client's request
     * @return Response chatbot
     */
    String chat(Map<String, String> data);
    void adminChat(Map<String, String> data);

    boolean checkUserIdExits(String id);
    // Lấy groupId của User hiện tại
    int takeGroupIdByUserId(String id);
    // Thêm người dùng mới (sẽ sort groupId và ++groupIdMaximum)
    boolean addNewChat(String id, String content, boolean isUser);
    // Lưu đoạn chat
    boolean saveContent(String id, String content, boolean isUser, int groupId);
    // Thêm đoạn chat
    boolean addUserChat(String id, String content);
    // Lấy dữ liệu đoạn chat
    List<ChatBotResponse> getMessages(int userId);

    List<GetAllChatResponse> getAllChat(String name);
    List<ChatBotResponse> getChatHistory(String userId);
}
