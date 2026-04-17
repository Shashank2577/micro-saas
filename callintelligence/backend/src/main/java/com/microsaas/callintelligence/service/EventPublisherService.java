package com.microsaas.callintelligence.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class EventPublisherService {
    
    public void publishEvent(String eventType, Map<String, Object> payload, UUID tenantId) {
        // In a real implementation this would publish to a message broker (e.g. RabbitMQ, Kafka)
        // For the scope of this microservice according to cc-starter framework, we log it.
        System.out.println("Publishing Event: " + eventType + " for tenant: " + tenantId);
        System.out.println("Payload: " + payload);
    }
}
