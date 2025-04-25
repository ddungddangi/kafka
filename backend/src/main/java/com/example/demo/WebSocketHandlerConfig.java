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
        logger.info(">>> WebSocketHandlerConfig 등록: /ws 핸들러 바인딩 시작");
        registry.addHandler(handler, "/ws")
                .setAllowedOriginPatterns("*");
        logger.info(">>> WebSocketHandlerConfig 완료: /ws 바인딩됨");
    }
}
