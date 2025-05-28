package com.example.bookingserver.application.command.command.chatbot;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ChatMessageCommand {
    String requestType;
    Map<String, String> data;
}
