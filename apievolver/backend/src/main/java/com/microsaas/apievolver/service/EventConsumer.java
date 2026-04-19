package com.microsaas.apievolver.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Service
@Slf4j
public class EventConsumer {
    public void consume(String eventType, UUID tenantId, String payload) {
        log.info("Consumed event: {} for tenant: {} with payload: {}", eventType, tenantId, payload);
    }
}
