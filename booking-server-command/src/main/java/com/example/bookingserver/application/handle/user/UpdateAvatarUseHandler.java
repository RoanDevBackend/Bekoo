package com.example.bookingserver.application.handle.user;

import com.cloudinary.Cloudinary;
import com.example.bookingserver.application.command.user.UpdateAvatarUserCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateAvatarUseHandler implements Handler<UpdateAvatarUserCommand> {

    final Cloudinary cloudinary;
    final UserRepository userRepository;
    @Override
    @SneakyThrows
    public void execute(UpdateAvatarUserCommand command) {
        User user= userRepository.findById(command.getId())
                .orElseThrow(()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        try {
            Map responseByCloudinary = cloudinary.uploader().upload(command.getFileImage().getBytes(), Map.of());
            user.setLinkAvatar(responseByCloudinary.get("url") + "");
            userRepository.save(user);
        }catch (IOException e){
            log.error("ERR_FILE: " + e.getMessage());
            throw new BookingCareException(ErrorDetail.ERR_FILE);
        }
    }
}
