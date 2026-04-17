package com.microsaas.agentops.service;

import com.microsaas.agentops.dto.AgentRunRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.repository.AgentRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentRunService {

    private final AgentRunRepository runRepository;

    @Transactional
    public AgentRun startRun(UUID tenantId, AgentRunRequest request) {
        AgentRun run = new AgentRun();
        run.setTenantId(tenantId);
        run.setAgentId(request.getAgentId());
        run.setWorkflowId(request.getWorkflowId());
        run.setStartedAt(request.getStartedAt());
        run.setStatus("RUNNING");
        return runRepository.save(run);
    }

    @Transactional
    public AgentRun updateRun(UUID tenantId, UUID runId, AgentRunRequest request) {
        AgentRun run = runRepository.findByIdAndTenantId(runId, tenantId)
                .orElseThrow(() -> new RuntimeException("Run not found"));
        
        if (request.getStatus() != null) {
            run.setStatus(request.getStatus());
        }
        if (request.getCompletedAt() != null) {
            run.setCompletedAt(request.getCompletedAt());
        }
        if (request.getTokenCost() != null) {
            run.setTokenCost(request.getTokenCost());
        }
        return runRepository.save(run);
    }

    @Transactional(readOnly = true)
    public List<AgentRun> getRuns(UUID tenantId) {
        return runRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public AgentRun getRun(UUID tenantId, UUID runId) {
        return runRepository.findByIdAndTenantId(runId, tenantId)
                .orElseThrow(() -> new RuntimeException("Run not found"));
    }
}
