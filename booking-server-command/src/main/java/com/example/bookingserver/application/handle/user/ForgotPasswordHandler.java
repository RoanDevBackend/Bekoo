package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.ForgotPasswordCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.reponse.SmsResponse;
import com.example.bookingserver.application.service.SmsService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordHandler implements Handler<ForgotPasswordCommand> {

    final UserRepository userRepository;
    final RedisRepository redisRepository;
    final SmsService smsService;
    @Override
    @SneakyThrows
    public void execute(ForgotPasswordCommand command) {
        User user= userRepository.findByPhoneNumber(command.getPhoneNumber())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        Random random= new Random();
        int code= random.nextInt(100000, 999999);
        SmsResponse smsResponse= smsService.send(user.getPhoneNumber(), "BK-" + code + " là mã xác thực ứng dụng Bekoo của bạn, vui lòng không tiết lộ cho ai");
        redisRepository.set(user.getPhoneNumber(), code);
    }
}
