package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public ChatWebSocketHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        logger.info("[ChatWebSocketHandler] [1] [연결] WebSocket 세션 연결 완료 (Client → Backend) | sessionId='{}' | 현재 연결 수={}", session.getId(), sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();

        // 2. WebSocket 메시지 수신
        logger.info("[ChatWebSocketHandler] [2] [수신] WebSocket 메시지 수신 (Client → Backend) | sessionId='{}' | payload='{}'", session.getId(), payload);

        // 3. Kafka로 메시지 전송
        try {
            kafkaTemplate.send("chat", payload);
            logger.info("[ChatWebSocketHandler] [3] [전송] Kafka 메시지 전송 (Backend Producer → Kafka Broker) | topic='chat' | payload='{}'", payload);
        } catch (Exception e) {
            logger.error("[ChatWebSocketHandler] [Error] Kafka 메시지 전송 실패 | topic='chat' | payload='{}' | error='{}'", payload, e.getMessage(), e);
        }
    }

    // Kafka에서 수신한 메시지를 모든 WebSocket 세션에 브로드캐스트
    public static void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    logger.error("[ChatWebSocketHandler] [Error] WebSocket 브로드캐스트 실패 | sessionId='{}' | error='{}'", session.getId(), e.getMessage(), e);
                }
            }
        }
        logger.info("[ChatWebSocketHandler] [4] [브로드캐스트] Kafka 메시지 수신 및 WebSocket 브로드캐스트 (Kafka Broker → Backend Consumer) | payload='{}'", message);
    }
}
