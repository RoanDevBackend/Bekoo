package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.service.OnlinePayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping(value = "/payment")
@RequiredArgsConstructor
public class ResultPaymentController {
    
    final OnlinePayService onlinePayService;

    @Value("${web.url}")
    String url;

    @GetMapping("/result")
    public String onlinePaymentResult(@RequestParam String vnp_ResponseCode,
                                      @RequestParam String vnp_TxnRef,
                                      @RequestParam Long vnp_Amount,
                                      @RequestParam String vnp_PayDate,
                                      Model model) {
        String value = "";
        if(vnp_ResponseCode.equals("00")){
            onlinePayService.extractPay(vnp_TxnRef);
            value = "Thanh toán thành công!";
        }
        if(vnp_ResponseCode.equals("11"))
            value = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
        if(vnp_ResponseCode.equals("12")) 
            value = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
        if(vnp_ResponseCode.equals("13"))
            value = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
        if(vnp_ResponseCode.equals("24"))
            value = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
        if(vnp_ResponseCode.equals("51"))
            value = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
        if(vnp_ResponseCode.equals("65"))
            value = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
        if(vnp_ResponseCode.equals("75"))
            value = "Ngân hàng thanh toán đang bảo trì.";
        if(vnp_ResponseCode.equals("79"))
            value = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
        model.addAttribute("txt_result", value);
        model.addAttribute("txt_txnRef", vnp_TxnRef);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String amount = formatter.format(vnp_Amount);
        model.addAttribute("txt_amount", amount);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(vnp_PayDate, inputFormatter);
        String date = dateTime.format(outputFormatter);
        model.addAttribute("txt_date", date);
        model.addAttribute("txt_url", url);
        return "payment-result";
    }
}
