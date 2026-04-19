package com.microsaas.contractportfolio.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventPublisher {

    public void publish(String eventType, Object payload) {
        // Integrate with webhook bus or Kafka
        log.info("Publishing event: {} with payload: {}", eventType, payload);
    }
}
