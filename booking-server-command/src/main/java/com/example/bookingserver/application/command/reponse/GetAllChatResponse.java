package com.example.bookingserver.application.command.reponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllChatResponse {
    String senderId;
    String userId;
    String name;
    String content;
    String time;
}
