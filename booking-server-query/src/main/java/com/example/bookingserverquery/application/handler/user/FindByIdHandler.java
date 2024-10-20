package com.example.bookingserverquery.application.handler.user;

import com.example.bookingserverquery.application.handler.exception.BookingCareException;
import com.example.bookingserverquery.application.handler.exception.ErrorDetail;
import com.example.bookingserverquery.application.reponse.user.FindByUserIdResponse;
import com.example.bookingserverquery.domain.User;
import com.example.bookingserverquery.domain.repository.UserRepository;
import com.example.bookingserverquery.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindByIdHandler {

    final UserRepository userRepository;
    final UserMapper userMapper;

    @SneakyThrows
    public FindByUserIdResponse execute(String id){
        Optional<User> userOptional= userRepository.findById(id);
        User user= userOptional.orElseThrow(
                () -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED)
        );
        return userMapper.toIdResponse(user);
    }
}
