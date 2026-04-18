package com.microsaas.usageintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UsageEventEmitter {

    private final ApplicationEventPublisher eventPublisher;

    public UsageEventEmitter(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void emitAnomalyDetected(String title, String recommendation) {
        UUID tenantId = TenantContext.require();
        // In a real scenario, this would publish to a distributed event bus like Kafka or RabbitMQ.
        // For the scope of cc-starter, we use ApplicationEventPublisher.
        eventPublisher.publishEvent(Map.of(
                "eventType", "usage.anomaly_detected",
                "tenantId", tenantId,
                "title", title,
                "recommendation", recommendation
        ));
    }

    public void emitCohortCreated(String cohortName) {
        UUID tenantId = TenantContext.require();
        eventPublisher.publishEvent(Map.of(
                "eventType", "usage.cohort_created",
                "tenantId", tenantId,
                "cohortName", cohortName
        ));
    }
}
