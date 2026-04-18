package com.microsaas.voiceagentbuilder.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.voiceagentbuilder.dto.AgentDto;
import com.microsaas.voiceagentbuilder.model.Agent;
import com.microsaas.voiceagentbuilder.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private AgentService agentService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreateAgent() {
        AgentDto dto = new AgentDto();
        dto.setName("Test Agent");
        dto.setSystemPrompt("You are a helpful agent.");

        Agent savedAgent = new Agent();
        savedAgent.setId(UUID.randomUUID());
        savedAgent.setTenantId(tenantId);
        savedAgent.setName("Test Agent");

        when(agentRepository.save(any(Agent.class))).thenReturn(savedAgent);

        Agent agent = agentService.createAgent(dto);

        assertNotNull(agent);
        assertEquals("Test Agent", agent.getName());
        verify(agentRepository, times(1)).save(any(Agent.class));
    }

    @Test
    void testGetAgent() {
        UUID agentId = UUID.randomUUID();
        Agent agent = new Agent();
        agent.setId(agentId);
        agent.setTenantId(tenantId);

        when(agentRepository.findByIdAndTenantId(agentId, tenantId)).thenReturn(Optional.of(agent));

        Agent found = agentService.getAgent(agentId);

        assertNotNull(found);
        assertEquals(agentId, found.getId());
    }

    @Test
    void testGetAgents() {
        when(agentRepository.findByTenantId(tenantId)).thenReturn(List.of(new Agent(), new Agent()));

        List<Agent> agents = agentService.getAgents();

        assertEquals(2, agents.size());
    }
}
