package com.microsaas.voiceagentbuilder.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.voiceagentbuilder.dto.AgentIntentDto;
import com.microsaas.voiceagentbuilder.model.AgentIntent;
import com.microsaas.voiceagentbuilder.repository.AgentIntentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentIntentService {
    private final AgentIntentRepository intentRepository;

    public List<AgentIntent> getIntents(UUID agentId) {
        return intentRepository.findByAgentIdAndTenantId(agentId, TenantContext.require());
    }

    public AgentIntent createIntent(UUID agentId, AgentIntentDto dto) {
        AgentIntent intent = AgentIntent.builder()
                .agentId(agentId)
                .tenantId(TenantContext.require())
                .name(dto.getName())
                .description(dto.getDescription())
                .actionType(dto.getActionType())
                .actionConfig(dto.getActionConfig())
                .build();
        return intentRepository.save(intent);
    }

    public void deleteIntent(UUID agentId, UUID id) {
        AgentIntent intent = intentRepository.findByIdAndAgentIdAndTenantId(id, agentId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Intent not found"));
        intentRepository.delete(intent);
    }
}
