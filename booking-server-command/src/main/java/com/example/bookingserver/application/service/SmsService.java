package com.example.bookingserver.application.service;

import com.example.bookingserver.application.reponse.SmsResponse;
import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    SmsResponse send(String to, String content);
}
