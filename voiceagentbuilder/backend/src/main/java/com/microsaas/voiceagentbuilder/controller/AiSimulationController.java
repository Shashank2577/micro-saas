package com.microsaas.voiceagentbuilder.controller;

import com.microsaas.voiceagentbuilder.dto.AiSimulationRequest;
import com.microsaas.voiceagentbuilder.dto.AiSimulationResponse;
import com.microsaas.voiceagentbuilder.service.AiSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents/{agentId}/simulate")
@RequiredArgsConstructor
public class AiSimulationController {
    private final AiSimulationService simulationService;

    @PostMapping
    public AiSimulationResponse simulate(@PathVariable UUID agentId, @RequestBody AiSimulationRequest request) {
        return simulationService.simulate(agentId, request);
    }
}
