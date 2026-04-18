package com.microsaas.voiceagentbuilder.controller;

import com.microsaas.voiceagentbuilder.dto.AgentDto;
import com.microsaas.voiceagentbuilder.model.Agent;
import com.microsaas.voiceagentbuilder.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @GetMapping
    public List<Agent> getAgents() {
        return agentService.getAgents();
    }

    @PostMapping
    public Agent createAgent(@RequestBody AgentDto dto) {
        return agentService.createAgent(dto);
    }

    @GetMapping("/{id}")
    public Agent getAgent(@PathVariable UUID id) {
        return agentService.getAgent(id);
    }

    @PutMapping("/{id}")
    public Agent updateAgent(@PathVariable UUID id, @RequestBody AgentDto dto) {
        return agentService.updateAgent(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAgent(@PathVariable UUID id) {
        agentService.deleteAgent(id);
    }
}
