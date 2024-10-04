package com.example.bookingserver.application.service;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendMail(String email, String content, boolean isHtml);
    void sendSms(String phoneNumber, String content, boolean isHtml);
}
