package com.rahul.instagram.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private static final String TOPIC = "notification-events";

    public void sendNotification(NotificationEvent event){
        kafkaTemplate.send(TOPIC, event);
    }
}
