package com.example.bookingserver.infrastructure.config;

import com.example.bookingserver.application.command.handle.chatbot.ChatBotHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    ChatBotHandler chatbotHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatbotHandler, "/chat").setAllowedOrigins("*");
    }
}