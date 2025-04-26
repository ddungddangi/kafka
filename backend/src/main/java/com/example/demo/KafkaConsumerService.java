package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void listen(String message) {
        try {
            // 5. Kafka broker → Backend Consumer 수신
            logger.info("[KafkaConsumerService] [5] [수신] Kafka Broker → Backend Consumer | payload='{}'", message);

            // 6. WebSocket 브로드캐스트
            ChatWebSocketHandler.broadcast(message);

            logger.info("[KafkaConsumerService] [6] [브로드캐스트] Backend Consumer → WebSocket Sessions | payload='{}'", message);
        } catch (Exception e) {
            logger.error("[KafkaConsumerService] [Error] Kafka 수신 또는 브로드캐스트 실패 | message='{}' | error='{}'", message, e.getMessage(), e);
        }
    }
}
