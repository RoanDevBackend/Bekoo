package com.example.bookingserver.application.command.handle.user;

import com.example.bookingserver.application.command.command.user.VerifyOTPCommand;
import com.example.bookingserver.application.command.handle.Handler;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VerifyOTPHandler implements Handler<VerifyOTPCommand> {

    final RedisRepository redisRepository;
    @Override
    @SneakyThrows
    public void execute(VerifyOTPCommand command) {
        Integer codeForOncePerson=redisRepository.get(command.getEmail());
        if(codeForOncePerson == null || !codeForOncePerson.equals(command.getCode())){
            throw new BookingCareException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }
    }
}
