package com.microsaas.insuranceai.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class UserCreatedEventListener {

    @EventListener
    public void handleUserCreatedEvent(Map<String, Object> event) {
        if ("user.created".equals(event.get("type"))) {
            // Process the consumed event as specified in the integration-manifest
            System.out.println("Received user.created event: " + event);
        }
    }
}
