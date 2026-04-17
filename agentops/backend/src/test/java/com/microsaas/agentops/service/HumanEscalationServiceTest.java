package com.microsaas.agentops.service;

import com.microsaas.agentops.dto.EscalationRequest;
import com.microsaas.agentops.dto.ResolveEscalationRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.model.HumanEscalation;
import com.microsaas.agentops.repository.AgentRunRepository;
import com.microsaas.agentops.repository.HumanEscalationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HumanEscalationServiceTest {

    @Mock
    private HumanEscalationRepository escalationRepository;
    
    @Mock
    private AgentRunRepository runRepository;

    @InjectMocks
    private HumanEscalationService escalationService;

    @Test
    void escalate() {
        UUID tenantId = UUID.randomUUID();
        UUID runId = UUID.randomUUID();
        
        EscalationRequest request = new EscalationRequest();
        request.setReason("Hallucination detected");
        
        AgentRun run = new AgentRun();
        run.setId(runId);
        run.setStatus("RUNNING");
        
        when(runRepository.findByIdAndTenantId(runId, tenantId)).thenReturn(Optional.of(run));
        
        HumanEscalation escalation = new HumanEscalation();
        escalation.setId(UUID.randomUUID());
        escalation.setStatus("PENDING");
        
        when(escalationRepository.save(any(HumanEscalation.class))).thenReturn(escalation);
        
        HumanEscalation result = escalationService.escalate(tenantId, runId, request);
        
        assertEquals("ESCALATED", run.getStatus());
        assertEquals("PENDING", result.getStatus());
        verify(runRepository).save(run);
        verify(escalationRepository).save(any(HumanEscalation.class));
    }

    @Test
    void resolveEscalation() {
        UUID tenantId = UUID.randomUUID();
        UUID escalationId = UUID.randomUUID();
        
        ResolveEscalationRequest request = new ResolveEscalationRequest();
        request.setResolution("Fixed manually");
        
        AgentRun run = new AgentRun();
        run.setStatus("ESCALATED");
        
        HumanEscalation escalation = new HumanEscalation();
        escalation.setId(escalationId);
        escalation.setStatus("PENDING");
        escalation.setRun(run);
        
        when(escalationRepository.findByIdAndTenantId(escalationId, tenantId)).thenReturn(Optional.of(escalation));
        when(escalationRepository.save(any(HumanEscalation.class))).thenReturn(escalation);
        
        HumanEscalation result = escalationService.resolveEscalation(tenantId, escalationId, request);
        
        assertEquals("RESOLVED", result.getStatus());
        assertEquals("COMPLETED", run.getStatus());
        verify(runRepository).save(run);
        verify(escalationRepository).save(escalation);
    }
}
