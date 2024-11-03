package com.example.bookingserver.application.command.handle.user;

import com.cloudinary.Cloudinary;
import com.example.bookingserver.application.command.command.user.UpdateAvatarUserCommand;
import com.example.bookingserver.application.command.handle.HandlerDTO;
import com.example.bookingserver.application.command.handle.exception.BookingCareException;
import com.example.bookingserver.application.command.handle.exception.ErrorDetail;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.constant.ApplicationConstant;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import document.event.user.UpdateAvatarUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateAvatarUseHandler implements HandlerDTO<UpdateAvatarUserCommand, String> {

    final Cloudinary cloudinary;
    final UserRepository userRepository;
    final UserMapper userMapper;
    final MessageProducer messageProducer;
    final String TOPIC= TopicConstant.UserTopic.UPDATE_AVATAR_USER;
    @Override
    @SneakyThrows
    public String execute(UpdateAvatarUserCommand command) {
        User user= userRepository.findById(command.getId())
                .orElseThrow(()-> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        String url_data;
        try {
            Map responseByCloudinary = cloudinary.uploader().upload(command.getFileImage().getBytes(), Map.of());

            url_data= responseByCloudinary.get("url") + "";
            user.setLinkAvatar(url_data);
            userRepository.save(user);

            UpdateAvatarUserEvent event= userMapper.fromUserToUpdateAvatarEvent(user);
            messageProducer.sendMessage(TOPIC, ApplicationConstant.EventType.UPDATE, event, event.getId(), "User");
        }catch (IOException e){
            log.error("ERR_FILE: {}", e.getMessage());
            throw new BookingCareException(ErrorDetail.ERR_FILE);
        }
        return url_data;
    }
}
