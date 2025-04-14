package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.chatbot.ChatMessageCommand;
import com.example.bookingserver.application.command.handle.chatbot.ChatBotHandler;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.reponse.ChatBotResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import com.example.bookingserver.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotController {

    ChatBotService chatBotService;
    ChatBotHandler chatBotHandler;
    UserRepository userRepository;
//
//    @PostMapping
//    @SecurityRequirement(name="bearerAuth")
//    public ApiResponse sendMessage(@RequestBody @Valid ChatMessageCommand request) throws IOException {
//        String content = request.getContent();
//        String senderId = request.getSenderId();
//        String botResponse = "";
//        if (chatBotService.addUserChat(senderId, content)){
//            botResponse = chatBotService.askAI(content, senderId);
//        }
//        return ApiResponse.success(200, "Gửi tin nhắn thành công!", botResponse);
//    }
//
    @GetMapping
    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Lấy dữ liệu tin nhắn của User và Bot", parameters = {
            @Parameter(name = "type", description = "0: Bot. 1: User")
    })
    public ApiResponse getMessage(@RequestParam(name = "senderId") String senderId){
        if (!chatBotService.checkUserIdExits(senderId)){
            String response = "Chưa có dữ liệu trước đây";
            return ApiResponse.error(404, response);
        }
        List<ChatBotResponse> responses = chatBotService.getMessages(chatBotService.takeGroupIdByUserId(senderId));
        return ApiResponse.success(200, "Lấy dữ liệu thành công", responses);
    }

//    ChatBotHandler chatBotHandler;
//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> req) throws IOException {
//        String userId = req.get("userId");
//        String message = req.get("message");
//
//        chatBotHandler.sendMessageToUser(userId, message);
//
//        return ResponseEntity.ok("Gửi thành công!");
//    }


}
