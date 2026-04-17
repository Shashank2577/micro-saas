package com.microsaas.agentops.service;

import com.microsaas.agentops.dto.EscalationRequest;
import com.microsaas.agentops.dto.ResolveEscalationRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.model.HumanEscalation;
import com.microsaas.agentops.repository.AgentRunRepository;
import com.microsaas.agentops.repository.HumanEscalationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HumanEscalationService {

    private final HumanEscalationRepository escalationRepository;
    private final AgentRunRepository runRepository;

    @Transactional
    public HumanEscalation escalate(UUID tenantId, UUID runId, EscalationRequest request) {
        AgentRun run = runRepository.findByIdAndTenantId(runId, tenantId)
                .orElseThrow(() -> new RuntimeException("Run not found"));
        
        run.setStatus("ESCALATED");
        runRepository.save(run);

        HumanEscalation escalation = new HumanEscalation();
        escalation.setTenantId(tenantId);
        escalation.setRun(run);
        escalation.setReason(request.getReason());
        escalation.setContext(request.getContext());
        escalation.setStatus("PENDING");
        
        return escalationRepository.save(escalation);
    }

    @Transactional
    public HumanEscalation resolveEscalation(UUID tenantId, UUID escalationId, ResolveEscalationRequest request) {
        HumanEscalation escalation = escalationRepository.findByIdAndTenantId(escalationId, tenantId)
                .orElseThrow(() -> new RuntimeException("Escalation not found"));
        
        escalation.setStatus("RESOLVED");
        escalation.setResolution(request.getResolution());
        escalation.setResolvedAt(LocalDateTime.now());
        
        AgentRun run = escalation.getRun();
        run.setStatus("COMPLETED");
        runRepository.save(run);
        
        return escalationRepository.save(escalation);
    }

    @Transactional(readOnly = true)
    public List<HumanEscalation> getPendingEscalations(UUID tenantId) {
        return escalationRepository.findByTenantIdAndStatus(tenantId, "PENDING");
    }
}
