package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
public class MessageServiceImpl implements MessageService {

    final JavaMailSender javaMailSender;

    @Override
    @SneakyThrows
    @Async
    public void sendMail(String email, String content, boolean isHtml) {
        var message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("roan.dev.backend@gmail.com", "Bekoo- Sức khoẻ tận tay");
        helper.setTo(email);
        helper.setSubject("Mã xác thực tài khoản của bạn tại Bekoo");
        helper.setText(content, isHtml);
        javaMailSender.send(message);

    }

    @Override
    public void sendSms(String phoneNumber, String content, boolean isHtml) {

    }
}
