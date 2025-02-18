package com.example.stock_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActivityProducer
{
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user-activity";

    public void sendActivity(String message)
    {
        log.info("Sending user activity: {}", message);
        kafkaTemplate.send(TOPIC, message);
    }
}
