package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.service.OnlinePayService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Map;

@Controller
@RequestMapping(value = "/payment")
@RequiredArgsConstructor
public class ResultPaymentController {
    
    final OnlinePayService onlinePayService;

    @Value("${web.url}")
    String url;

    @GetMapping("/result")
    public String onlinePaymentResult(@RequestParam Map<String, String> params,
                                      Model model,
                                      HttpServletRequest httpServletRequest) {
        String value = onlinePayService.extractPay(params, httpServletRequest);
        model.addAttribute("txt_result", value);
        model.addAttribute("txt_txnRef", params.get("vnp_TxnRef"));
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String amount = formatter.format(Long.parseLong(params.get("vnp_Amount"))/ 100);
        model.addAttribute("txt_amount", amount);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(params.get("vnp_PayDate"), inputFormatter);
        String date = dateTime.format(outputFormatter);
        model.addAttribute("txt_date", date);
        model.addAttribute("txt_url", url);
        return "payment-result";
    }
}
