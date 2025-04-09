package com.example.bookingserver.application.command.service;

public interface ChatBotService {
    // Kiểm tra xem người dùng đã từng chat hay chưa
    boolean checkUserIdExits(Long id);
    // Lấy groupId của User hiện tại
    int takeGroupIdByUserId(Long id);
    // Thêm người dùng mới (sẽ sort groupId và ++groupIdMaximum)
    boolean addNewUser(Long id, String content);
    // Lưu đoạn chat
    boolean saveContent(Long id, String content, boolean isUser, int groupId);
}
