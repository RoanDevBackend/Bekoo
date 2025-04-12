package com.example.bookingserver.application.command.command.chatbot;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageCommand {
    @NotBlank
    String content;
    @NotBlank
    String senderId;
    @NotNull
    LocalDateTime timestamp;
}
