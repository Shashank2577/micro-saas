package com.microsaas.peopleanalytics.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void emit(String eventType, UUID tenantId, Map<String, Object> payload) {
        DomainEvent event = new DomainEvent(
                UUID.randomUUID(),
                tenantId,
                eventType,
                OffsetDateTime.now(),
                "peopleanalytics",
                payload
        );
        applicationEventPublisher.publishEvent(event);
        // Real implementation would send to Kafka / pgmq here based on integration-manifest
    }
}
