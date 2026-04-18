package com.microsaas.taskqueue.service;

import java.util.Map;

public class TaskQueueEvent {
    private final String eventType;
    private final Map<String, Object> payload;

    public TaskQueueEvent(String eventType, Map<String, Object> payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public String getEventType() { return eventType; }
    public Map<String, Object> getPayload() { return payload; }
}
