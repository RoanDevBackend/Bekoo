package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.chatbot.ChatMessageCommand;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotController {

    ChatBotService chatBotService;

    @PostMapping
    public ApiResponse sendMessage(@RequestBody @Valid ChatMessageCommand request) throws IOException {
        String content = request.getContent();
        String senderId = request.getSenderId();
        String botResponse = "";
        if (chatBotService.addUserChat(senderId, content)){
            botResponse = chatBotService.askAI(content, senderId);
        }
        return ApiResponse.success(200, "Gửi tin nhắn thành công!", botResponse);
    }

    @GetMapping
    @Operation(summary = "Lấy dữ liệu tin nhắn của User và Bot", parameters = {
            @Parameter(name = "type", description = "0: Bot. 1: User")
    })
    public ApiResponse getMessage(@RequestParam(name = "senderId") String senderId){
        List<ChatBotResponse> responses = chatBotService.getMessages(chatBotService.takeGroupIdByUserId(senderId));
        return ApiResponse.success(200, "Lấy dữ liệu thành công", responses);
    }
}
