package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener; // Kafka 메시지를 수신하기 위한 어노테이션
import org.springframework.stereotype.Service;

@Service // Spring이 이 클래스를 Service로 관리하도록 지정
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    // Kafka의 'chat' 토픽에서 메시지를 수신하는 메서드
    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void listen(String message) {
        System.out.println("Kafka에서 수신한 메시지: " + message);
        try {
            ChatWebSocketHandler.broadcast(message); // 수신한 메시지를 모든 클라이언트에게 브로드캐스트
            logger.info("---- broadcast message={}----", message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("---- broadcast error={}----",e);
        }
    }
}
