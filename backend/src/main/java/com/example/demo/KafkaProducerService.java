package com.example.demo.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message);
            logger.info("[KafkaProducerService] [3] [전송] Backend Producer → Kafka Broker | topic='{}' | payload='{}'", topic, message);
        } catch (Exception e) {
            logger.error("[KafkaProducerService] [Error] Kafka 메시지 전송 실패 | topic='{}' | payload='{}' | error='{}'", topic, message, e.getMessage(), e);
        }
    }
}
