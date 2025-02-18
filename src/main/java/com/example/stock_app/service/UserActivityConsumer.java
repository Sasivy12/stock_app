package com.example.stock_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserActivityConsumer
{
    @KafkaListener(topics = "user-activity", groupId = "stock_app_group")
    public void consume(String message)
    {
        log.info("User acitvity recieved {}", message);
    }
}
