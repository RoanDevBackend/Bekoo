package com.example.bookingserver.controller;

import com.example.bookingserver.application.command.command.chatbot.ChatMessageCommand;
import com.example.bookingserver.application.command.reponse.ApiResponse;
import com.example.bookingserver.application.command.service.ChatBotService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotController {

    RestTemplate restTemplate;
    SimpMessagingTemplate messagingTemplate;
    ChatBotService chatBotService;


    // Nhận request từ người dùng và gửi cho AI
    @PostMapping
    public ApiResponse sendMessage(@RequestBody @Valid ChatMessageCommand request) {
        String content = request.getContent();
        String senderId = request.getSenderId();

        // Nếu nhận request thành công
        if (chatBotService.addUserChat(senderId, content)){

        }

//        // Gửi tới hệ thống AI
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Payload gửi sang hệ thống AI
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("userId", senderId);
//        payload.put("message", content);
//        payload.put("callbackUrl", "https://bekoo.vercel.app/api/webhook/ai-response");
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
//        restTemplate.postForEntity("http://ai-server.com/api/chat", entity, String.class);


        return ApiResponse.success(200, "Gửi tin nhắn thành công!");
    }

    @PostMapping("/ai-response")
    public ApiResponse receiveAIResponse(@RequestBody ChatMessageCommand response) {
        String userId = response.getSenderId();
        String botReply = response.getContent();

        // Gửi qua WebSocket đến client
        messagingTemplate.convertAndSend("/topic/reply/" + userId, new ChatMessageCommand(botReply, userId, LocalDateTime.now()));

        return ApiResponse.success(200, "Gửi tin nhắn thành công!", botReply);
    }
    //    Khi AI gửi phản hồi, server bạn sẽ:
    //    Nhận dữ liệu tại /api/webhook/ai-response
    //    Gửi ngay về client theo WebSocket path: /topic/reply/{senderId}
    //→ Frontend (nếu dùng SockJS hoặc STOMP) chỉ cần subscribe đúng channel là sẽ nhận được phản hồi realtime.
}
