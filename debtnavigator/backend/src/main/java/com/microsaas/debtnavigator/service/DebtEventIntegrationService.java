package com.microsaas.debtnavigator.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DebtEventIntegrationService {

    public void publishEvent(String eventType, Object payload) {
        log.info("Publishing event: {} with payload: {}", eventType, payload);
    }

    public void consumeEvent(String eventType, Object payload) {
        log.info("Consumed event: {} with payload: {}", eventType, payload);
    }
}
