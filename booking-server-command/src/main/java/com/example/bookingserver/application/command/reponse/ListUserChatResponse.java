package com.example.bookingserver.application.command.reponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListUserChatResponse {
    UserResponse userResponse;
    int groupId;
    String lastestMessage;
    LocalDateTime time;

}
