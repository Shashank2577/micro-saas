package com.microsaas.workspacemanager.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(String eventName, Object payload) {
        applicationEventPublisher.publishEvent(new WorkspaceEvent(eventName, payload));
    }

    public static class WorkspaceEvent {
        private final String name;
        private final Object payload;

        public WorkspaceEvent(String name, Object payload) {
            this.name = name;
            this.payload = payload;
        }

        public String getName() { return name; }
        public Object getPayload() { return payload; }
    }
}
