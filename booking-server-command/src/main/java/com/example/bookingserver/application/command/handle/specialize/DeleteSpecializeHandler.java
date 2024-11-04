package com.example.bookingserver.application.command.handle.specialize;


import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import com.example.bookingserver.infrastructure.message.MessageProducer;
import document.constant.TopicConstant;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DeleteSpecializeHandler {

    private SpecializeRepository specializeRepository;
    String TOPIC= TopicConstant.SpecializeTopic.DELETE_SPECIALIZE;
    private MessageProducer messageProducer;


    @SneakyThrows
    @Transactional
    public void execute(List<String> ids){
        for(String id: ids){
            Optional<Specialize> specializeOptional= specializeRepository.findById(id);
            if(specializeOptional.isPresent()) {
                try {
                    specializeRepository.delete(id);
                    messageProducer.sendMessage(TOPIC, "DELETE", id, id, "Specialize");
                }catch (Exception e) {
                    throw new RuntimeException("Hiện không thể xoá khoa này");
                }
            }
        }
    }
}
