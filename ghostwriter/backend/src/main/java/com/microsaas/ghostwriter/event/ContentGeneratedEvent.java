package com.microsaas.ghostwriter.event;

import java.util.UUID;
import java.util.Map;

public class ContentGeneratedEvent {
    private String eventId;
    private UUID tenantId;
    private String sourceApp;
    private Map<String, Object> payload;

    public ContentGeneratedEvent(String eventId, UUID tenantId, String sourceApp, Map<String, Object> payload) {
        this.eventId = eventId;
        this.tenantId = tenantId;
        this.sourceApp = sourceApp;
        this.payload = payload;
    }

    public String getEventId() { return eventId; }
    public UUID getTenantId() { return tenantId; }
    public String getSourceApp() { return sourceApp; }
    public Map<String, Object> getPayload() { return payload; }
}
