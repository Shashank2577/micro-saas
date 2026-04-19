package com.microsaas.financenarrator.service;

import com.crosscutting.starter.queue.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final QueueService queueService;

    public void publishEvent(String eventType, UUID tenantId, String sourceApp, Map<String, Object> payload) {
        log.info("Emitting event: {} for tenant {} from app {}", eventType, tenantId, sourceApp);
        String payloadString = payload.toString();
        queueService.enqueue("events", payloadString, 0);
    }
}
