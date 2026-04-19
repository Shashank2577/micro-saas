package com.micro.interviewos.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
import java.util.UUID;
import java.time.ZonedDateTime;

@Getter
public class InterviewEvent extends ApplicationEvent {
    private final String eventType;
    private final UUID tenantId;
    private final Map<String, Object> payload;
    private final UUID eventId;
    private final ZonedDateTime occurredAt;
    private final String sourceApp = "interviewos";

    public InterviewEvent(String eventType, UUID tenantId, Map<String, Object> payload) {
        super(payload);
        this.eventType = eventType;
        this.tenantId = tenantId;
        this.payload = payload;
        this.eventId = UUID.randomUUID();
        this.occurredAt = ZonedDateTime.now();
    }
}
