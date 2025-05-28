package com.example.bookingserver.application.command.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotResponse {
    String content;
    String createdBy;
    String createdAt;
}
