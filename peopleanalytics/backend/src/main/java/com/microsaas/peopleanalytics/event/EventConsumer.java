package com.microsaas.peopleanalytics.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventConsumer {

    // Consumes: performancenarrative.review.finalized, retentionsignal.risk.detected, onboardflow.milestone.completed

    @EventListener
    public void handleDomainEvent(DomainEvent event) {
        switch (event.getEventType()) {
            case "performancenarrative.review.finalized":
                log.info("Received performance narrative review finalized event: {}", event.getEventId());
                break;
            case "retentionsignal.risk.detected":
                log.info("Received retention risk detected event: {}", event.getEventId());
                break;
            case "onboardflow.milestone.completed":
                log.info("Received onboard flow milestone completed event: {}", event.getEventId());
                break;
            default:
                // Ignore other events locally or log at trace level
                break;
        }
    }
}
