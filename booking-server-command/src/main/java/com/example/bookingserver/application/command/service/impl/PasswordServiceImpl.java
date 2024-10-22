package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    final PasswordEncoder passwordEncoder;
    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
