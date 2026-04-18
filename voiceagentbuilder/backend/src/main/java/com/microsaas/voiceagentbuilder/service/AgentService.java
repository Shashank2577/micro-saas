package com.microsaas.voiceagentbuilder.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.voiceagentbuilder.dto.AgentDto;
import com.microsaas.voiceagentbuilder.model.Agent;
import com.microsaas.voiceagentbuilder.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;

    public List<Agent> getAgents() {
        return agentRepository.findByTenantId(TenantContext.require());
    }

    public Agent getAgent(UUID id) {
        return agentRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
    }

    public Agent createAgent(AgentDto dto) {
        Agent agent = Agent.builder()
                .tenantId(TenantContext.require())
                .name(dto.getName())
                .description(dto.getDescription())
                .systemPrompt(dto.getSystemPrompt())
                .voiceId(dto.getVoiceId())
                .language(dto.getLanguage() != null ? dto.getLanguage() : "en-US")
                .provider("litellm")
                .status("DRAFT")
                .build();
        return agentRepository.save(agent);
    }

    public Agent updateAgent(UUID id, AgentDto dto) {
        Agent agent = getAgent(id);
        agent.setName(dto.getName());
        agent.setDescription(dto.getDescription());
        agent.setSystemPrompt(dto.getSystemPrompt());
        agent.setVoiceId(dto.getVoiceId());
        agent.setLanguage(dto.getLanguage());
        return agentRepository.save(agent);
    }

    public void deleteAgent(UUID id) {
        Agent agent = getAgent(id);
        agentRepository.delete(agent);
    }
}
