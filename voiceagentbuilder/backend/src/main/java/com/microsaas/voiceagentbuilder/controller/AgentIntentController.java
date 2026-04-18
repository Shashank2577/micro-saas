package com.microsaas.voiceagentbuilder.controller;

import com.microsaas.voiceagentbuilder.dto.AgentIntentDto;
import com.microsaas.voiceagentbuilder.model.AgentIntent;
import com.microsaas.voiceagentbuilder.service.AgentIntentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents/{agentId}/intents")
@RequiredArgsConstructor
public class AgentIntentController {
    private final AgentIntentService intentService;

    @GetMapping
    public List<AgentIntent> getIntents(@PathVariable UUID agentId) {
        return intentService.getIntents(agentId);
    }

    @PostMapping
    public AgentIntent createIntent(@PathVariable UUID agentId, @RequestBody AgentIntentDto dto) {
        return intentService.createIntent(agentId, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteIntent(@PathVariable UUID agentId, @PathVariable UUID id) {
        intentService.deleteIntent(agentId, id);
    }
}
