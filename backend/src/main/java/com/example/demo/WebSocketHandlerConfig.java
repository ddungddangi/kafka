package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.ChatWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandlerConfig.class);
    private final ChatWebSocketHandler handler;

    public WebSocketHandlerConfig(ChatWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            logger.info("[WebSocketHandlerConfig] WebSocket 핸들러 등록 시작: 경로='/ws'");
            registry.addHandler(handler, "/ws")
                    .setAllowedOriginPatterns("*");
            logger.info("[WebSocketHandlerConfig] WebSocket 핸들러 등록 완료: 경로='/ws'");
        } catch (Exception e) {
            logger.error("[WebSocketHandlerConfig] [Error] WebSocket 핸들러 등록 실패 | error='{}'", e.getMessage(), e);
        }
    }
}
