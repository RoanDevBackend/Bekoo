package com.example.bookingserver.application.command.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface OnlinePayService {
    String payCart(String scheduleId, HttpServletRequest req);
//    int extractPay(int vnpay_responseCode , int  vnp_TxnRef);
}
