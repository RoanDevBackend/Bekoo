package com.example.bookingserver.application.command.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface OnlinePayService {
    String payCart(String scheduleId, HttpServletRequest req);
    String extractPay(Map<String, String> params, HttpServletRequest request);
}
