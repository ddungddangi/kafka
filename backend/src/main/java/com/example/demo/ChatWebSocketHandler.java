package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*---Log 추가----*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    /* ---Log 추가----*/
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);


    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public ChatWebSocketHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);  // 연결된 세션 저장
         /* ---Log 추가 ----*/
        logger.info("새 세션 연결됨: {}, 현재 세션 수: {}", session.getId(), sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        /* ---Log 추가*/
        String payload = message.getPayload();
        logger.info("세션 {} 로부터 메시지 수신: {}", session.getId(), payload);
        /* ---*/
        kafkaTemplate.send("chat", message.getPayload()); // 메시지를 Kafka로 보냄

        /* ---Log 추가*/
        logger.info("Kafka로 보냄 → topic='chat', payload='{}'", payload);
    }

    // Kafka에서 수신한 메시지를 모든 클라이언트에게 브로드캐스트하는 메서드
    public static void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) { // 세션이 열려있는 경우에만 메시지를 보냄
                try {
                    session.sendMessage(new TextMessage(message)); // 클라이언트에게 메시지 전송
                } catch (IOException e) {
                    logger.error("세션 {} 에게 메시지 전송 실패", session.getId(), e);
                }
            }
        }
        logger.info("브로드캐스트 완료: {}", message);
    }
}
