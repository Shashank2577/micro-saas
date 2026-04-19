package com.microsaas.peopleanalytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainEvent {
    private UUID eventId;
    private UUID tenantId;
    private String eventType;
    private OffsetDateTime occurredAt;
    private String sourceApp;
    private Map<String, Object> payload;
}
