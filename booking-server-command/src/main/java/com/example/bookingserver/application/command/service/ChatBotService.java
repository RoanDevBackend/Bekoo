package com.example.bookingserver.application.command.service;

public interface ChatBotService {
    // Kiểm tra xem người dùng đã từng chat hay chưa
    boolean checkUserIdExits(String id);
    // Lấy groupId của User hiện tại
    int takeGroupIdByUserId(String id);
    // Thêm người dùng mới (sẽ sort groupId và ++groupIdMaximum)
    boolean addNewChat(String id, String content, boolean isUser);
    // Lưu đoạn chat
    boolean saveContent(String id, String content, boolean isUser, int groupId);
}
