package com.microsaas.usageintelligence.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UsageEventConsumer {

    @EventListener
    public void handleUserEvents(Map<String, Object> eventData) {
        if (!eventData.containsKey("eventType")) return;
        
        String eventType = (String) eventData.get("eventType");
        if ("user.created".equals(eventType) || "user.deleted".equals(eventType)) {
            // Process user events based on integration-manifest
            System.out.println("Received user event: " + eventType);
        }
    }
}
