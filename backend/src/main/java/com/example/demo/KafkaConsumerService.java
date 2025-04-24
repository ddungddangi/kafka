package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConsumerService {
    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void listen(String message) throws IOException {
        ChatWebSocketHandler.broadcast(message); // Kafka에서 받은 메시지를 모든 클라이언트에 전달
    }
}
