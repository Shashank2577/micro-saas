package com.microsaas.apimanager.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EventIntegrationService {

    // Simulating consumtion of tenant events
    @EventListener(condition = "#event.name == 'tenant.created'")
    public void onTenantCreated(Object event) {
        System.out.println("Tenant created event received.");
    }

    @EventListener(condition = "#event.name == 'tenant.deleted'")
    public void onTenantDeleted(Object event) {
        System.out.println("Tenant deleted event received.");
    }
}
