package com.example.jira.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDeletedEventListener {

    @KafkaListener(topics = "user-deleted-event", groupId = "ticket-service-group")
    public void consumeUserDeletedEvent(String value) {
        log.info("User deleted event received: {}", value);

        // TODO : Add logic to close all tickets assigned to the deleted user, or reassign them to another user, etc.
    }
}
