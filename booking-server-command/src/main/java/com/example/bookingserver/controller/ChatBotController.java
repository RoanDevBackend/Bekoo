package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ListUserChatResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat/list-user")
@SecurityRequirement(name="bearerAuth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatBotController {

    ChatBotService chatBotService;

    @GetMapping
    public ApiResponse getListUser(@RequestParam("search-word") String word){
        List<ListUserChatResponse> response = chatBotService.getListUserChat();
        return ApiResponse.success(200, "Lay thanh cong", response);
    }
}
