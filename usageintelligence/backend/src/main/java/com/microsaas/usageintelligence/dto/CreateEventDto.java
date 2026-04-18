package com.microsaas.usageintelligence.dto;

import java.util.Map;

public class CreateEventDto {
    private String userId;
    private String eventName;
    private Map<String, Object> properties;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
