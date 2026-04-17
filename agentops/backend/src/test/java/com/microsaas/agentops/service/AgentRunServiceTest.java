package com.microsaas.agentops.service;

import com.microsaas.agentops.dto.AgentRunRequest;
import com.microsaas.agentops.model.AgentRun;
import com.microsaas.agentops.repository.AgentRunRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentRunServiceTest {

    @Mock
    private AgentRunRepository runRepository;

    @InjectMocks
    private AgentRunService runService;

    @Test
    void startRun() {
        UUID tenantId = UUID.randomUUID();
        AgentRunRequest request = new AgentRunRequest();
        request.setAgentId("agent-1");
        request.setWorkflowId("wf-1");
        request.setStartedAt(LocalDateTime.now());

        AgentRun run = new AgentRun();
        run.setId(UUID.randomUUID());
        run.setTenantId(tenantId);
        run.setAgentId(request.getAgentId());
        run.setStatus("RUNNING");

        when(runRepository.save(any(AgentRun.class))).thenReturn(run);

        AgentRun result = runService.startRun(tenantId, request);

        assertNotNull(result);
        assertEquals("RUNNING", result.getStatus());
        assertEquals("agent-1", result.getAgentId());
        verify(runRepository).save(any(AgentRun.class));
    }

    @Test
    void updateRun() {
        UUID tenantId = UUID.randomUUID();
        UUID runId = UUID.randomUUID();
        AgentRunRequest request = new AgentRunRequest();
        request.setStatus("COMPLETED");
        request.setTokenCost(new BigDecimal("0.05"));

        AgentRun run = new AgentRun();
        run.setId(runId);
        run.setTenantId(tenantId);
        run.setStatus("RUNNING");
        run.setTokenCost(BigDecimal.ZERO);

        when(runRepository.findByIdAndTenantId(runId, tenantId)).thenReturn(Optional.of(run));
        when(runRepository.save(any(AgentRun.class))).thenReturn(run);

        AgentRun result = runService.updateRun(tenantId, runId, request);

        assertEquals("COMPLETED", result.getStatus());
        assertEquals(new BigDecimal("0.05"), result.getTokenCost());
    }
}
