package com.example.bookingserver.application.command.handle.specialize;


import com.example.bookingserver.domain.Specialize;
import com.example.bookingserver.domain.repository.SpecializeRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeleteSpecializeHandler {

    SpecializeRepository specializeRepository;

    public void execute(List<String> ids){
        for(String id: ids){
            Optional<Specialize> specializeOptional= specializeRepository.findById(id);
            if(specializeOptional.isPresent()) {
                specializeRepository.delete(id);
                log.info("DELETE SPECIALIZE SUCCESS: {}", specializeOptional.get().toString());
            }
            else{
                log.error("SPECIALIZE NOT FOUND: {}", id);
            }
        }
    }
}
