package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.service.ChatMessageService;
import com.example.bookingserver.domain.ChatMessage;
import com.example.bookingserver.domain.repository.ChatMessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageServiceImpl implements ChatMessageService {
    private ChatMessageRepository chatMessageRepository;

    RestTemplate restTemplate = new RestTemplate();
    String AI_API_URL = "http://your-ai-api-endpoint"; // API AI
    String API_KEY = "your-api-key"; // API KEY

    @Override
    public ChatMessage saveMessage(String content, String sender) {
        ChatMessage message = new ChatMessage(content, sender);
        return chatMessageRepository.save(message);
    }

    @Override
    public String getAIResponse(String userMessage) {
        // Gọi API AI (giả sử API trả về JSON với field "response")
        String requestBody = "{\"message\": \"" + userMessage + "\"}";
        String response = restTemplate.postForObject(
                AI_API_URL + "?key=" + API_KEY,
                requestBody,
                String.class
        );
        // Xử lý response từ API AI (tùy thuộc định dạng API của bạn)
        return extractAIResponse(response); // Hàm này bạn tự viết để parse response
    }

    private String extractAIResponse(String response) {
        // Ví dụ: giả sử response là JSON {"response": "AI message"}
        return response; // Thay bằng logic parse thực tế
    }
}
