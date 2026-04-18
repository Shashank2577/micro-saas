package com.microsaas.voiceagentbuilder.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventPublisherService {
    private final ApplicationEventPublisher eventPublisher;

    public void publishCallCompleted(String tenantId, String agentId, String callId) {
        // Simple stub for emitting call.completed
    }
    
    public void publishAgentEscalated(String tenantId, String agentId, String callId, String reason) {
        // Simple stub for emitting agent.escalated
    }
}
