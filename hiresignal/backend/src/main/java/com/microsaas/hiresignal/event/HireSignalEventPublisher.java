package com.microsaas.hiresignal.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HireSignalEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void emitEvent(String topic, UUID tenantId, String sourceApp, Map<String, Object> payload) {
        Map<String, Object> event = Map.of(
                "event_id", UUID.randomUUID(),
                "tenant_id", tenantId,
                "occurred_at", ZonedDateTime.now(),
                "source_app", sourceApp,
                "payload", payload
        );

        log.info("Emitting event to topic: {} for tenant: {}", topic, tenantId);
        kafkaTemplate.send(topic, event);
    }

    public void publishCandidateShortlisted(UUID tenantId, UUID candidateId) {
        emitEvent("hiresignal.candidate.shortlisted", tenantId, "hiresignal", Map.of("candidateId", candidateId));
    }

    public void publishRiskFlagged(UUID tenantId, UUID candidateId, String riskType) {
        emitEvent("hiresignal.risk.flagged", tenantId, "hiresignal", Map.of("candidateId", candidateId, "riskType", riskType));
    }

    public void publishHireConfirmed(UUID tenantId, UUID decisionId) {
        emitEvent("hiresignal.hire.confirmed", tenantId, "hiresignal", Map.of("decisionId", decisionId));
    }
}
