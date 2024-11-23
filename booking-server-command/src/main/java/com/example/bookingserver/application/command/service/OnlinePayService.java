package com.example.bookingserver.application.command.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface OnlinePayService {
    String payCart(String scheduleId, HttpServletRequest req);
    void extractPay(String vnp_TxnRef);
}
