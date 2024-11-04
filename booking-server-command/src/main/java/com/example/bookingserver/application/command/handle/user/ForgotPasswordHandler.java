package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.command.user.ForgotPasswordCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.application.command.service.MessageService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class ForgotPasswordHandler implements Handler<ForgotPasswordCommand> {

    final UserRepository userRepository;
    final RedisRepository redisRepository;
    final MessageService messageService;
    final SpringTemplateEngine templateEngine;

    @Override
    @SneakyThrows
    public void execute(ForgotPasswordCommand command) {
        User user= userRepository.findByEmail(command.getEmail())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        Random random= new Random();
        int code= random.nextInt(100000, 999999);

        Context context= new Context();
        context.setVariable("code", code);
        context.setVariable("name", user.getName());
        String content= templateEngine.process("forgot-password", context);
        String subject= "Mã xác thực tài khoản";
        messageService.sendMail(subject, command.getEmail(), content, true);
        redisRepository.set(user.getEmail(), code);
        redisRepository.setTimeToLive(user.getEmail(), 60L);
    }
}
