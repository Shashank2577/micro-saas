package com.microsaas.datastoryteller.event;

import com.microsaas.datastoryteller.domain.model.NarrativeReport;
import com.microsaas.datastoryteller.service.NarrativePublishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventHandler {

    // Simple listener for internal domain events that could be translated
    // into cc-starter webhooks via its webhook module.
    @EventListener
    public void onNarrativePublished(NarrativePublishedEvent event) {
        NarrativeReport report = event.getReport();
        log.info("Narrative published event handled. ID: {}, Tenant: {}", report.getId(), report.getTenantId());
        // For testing purposes, we log this. If webhook module is configured, we would send it to nexus-hub.
        // We will assume cc-starter provides automatic webhook emission or we could use RestTemplate to POST.
        emitToNexus("narrative.published", report.getTenantId(), report.getId().toString());
    }

    public void emitToNexus(String eventType, String tenantId, String payload) {
        // Mock method to assert in tests
        log.info("Emitting event {} for tenant {} with payload {}", eventType, tenantId, payload);
    }
}
