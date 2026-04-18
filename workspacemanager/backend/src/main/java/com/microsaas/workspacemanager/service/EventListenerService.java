package com.microsaas.workspacemanager.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EventListenerService {

    @EventListener
    public void handleWorkspaceEvent(EventPublisher.WorkspaceEvent event) {
        if ("USER_SIGNED_UP".equals(event.getName())) {
            // Handle auto-provisioning logic based on SSO domain
            System.out.println("Received USER_SIGNED_UP event: " + event.getPayload());
        }
    }
}
