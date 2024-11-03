package com.example.bookingserver.application.command.service;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendMail(String subject, String email, String content, boolean isHtml);
    void sendSms(String phoneNumber, String content, boolean isHtml);
}
