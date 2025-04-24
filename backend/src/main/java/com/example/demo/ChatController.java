package com.example.demo.controller;

import com.example.demo.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final KafkaProducerService kafkaProducerService;

    @MessageMapping("/chat.send")  // React 프론트엔드에서 보낼 경로
    public void sendMessage(@Payload String message) {
        kafkaProducerService.sendMessage("chat", message);
    }
}
