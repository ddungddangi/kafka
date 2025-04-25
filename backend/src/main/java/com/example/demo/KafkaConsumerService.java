package com.example.demmo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat", groupId = "chat-group")
    public void listen(String message) {
        log.info("Kafka에서 받은 메시지: {}", message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
