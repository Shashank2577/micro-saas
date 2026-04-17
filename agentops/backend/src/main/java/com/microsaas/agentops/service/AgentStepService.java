package com.microsaas.agentops.service;

import com.microsaas.agentops.dto.AgentStepRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.model.AgentStep;
import com.microsaas.agentops.repository.AgentRunRepository;
import com.microsaas.agentops.repository.AgentStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentStepService {

    private final AgentStepRepository stepRepository;
    private final AgentRunRepository runRepository;

    @Transactional
    public AgentStep addStep(UUID tenantId, UUID runId, AgentStepRequest request) {
        AgentRun run = runRepository.findByIdAndTenantId(runId, tenantId)
                .orElseThrow(() -> new RuntimeException("Run not found"));

        AgentStep step = new AgentStep();
        step.setTenantId(tenantId);
        step.setRun(run);
        step.setStepType(request.getStepType());
        step.setInput(request.getInput());
        step.setOutput(request.getOutput());
        step.setDurationMs(request.getDurationMs());
        
        return stepRepository.save(step);
    }

    @Transactional(readOnly = true)
    public List<AgentStep> getStepsForRun(UUID tenantId, UUID runId) {
        return stepRepository.findByRunIdAndTenantId(runId, tenantId);
    }
}
