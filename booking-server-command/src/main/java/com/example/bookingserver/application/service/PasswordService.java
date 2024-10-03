package com.example.bookingserver.application.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    void encode(String password);
}
