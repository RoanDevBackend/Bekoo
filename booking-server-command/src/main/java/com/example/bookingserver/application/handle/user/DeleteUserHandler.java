package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.DeleteUserCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.infrastructure.mapper.UserMapper;
import com.example.bookingserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteUserHandler implements Handler<DeleteUserCommand> {

    final UserRepository userRepository;
    final UserMapper userMapper;

    @Override
    public void execute(DeleteUserCommand command) {
        List<String> ids= command.getIds();
        for(String id: ids){
            userRepository.delete(id);
        }
    }
}
