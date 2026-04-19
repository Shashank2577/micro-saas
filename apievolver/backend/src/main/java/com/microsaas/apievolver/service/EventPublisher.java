package com.microsaas.apievolver.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
@Slf4j
public class EventPublisher {
    public void publish(String eventType, UUID tenantId, String payloadData) {
        Map<String, Object> event = new HashMap<>();
        event.put("event_id", UUID.randomUUID().toString());
        event.put("tenant_id", tenantId.toString());
        event.put("occurred_at", ZonedDateTime.now().toString());
        event.put("source_app", "apievolver");
        event.put("payload", payloadData);

        log.info("Emitting structured event: {} -> {}", eventType, event);
    }
}
