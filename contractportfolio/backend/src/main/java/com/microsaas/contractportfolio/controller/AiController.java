package com.microsaas.contractportfolio.controller;

import com.microsaas.contractportfolio.service.AiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class AiController {

    private final AiClientService aiClientService;

    @PostMapping("/ai/analyze")
    public String analyze(@RequestBody Map<String, String> request) {
        return aiClientService.analyze(request.getOrDefault("prompt", "Analyze contract"));
    }

    @PostMapping("/ai/recommendations")
    public String recommendations(@RequestBody Map<String, String> request) {
        return aiClientService.recommend(request.getOrDefault("prompt", "Give recommendations"));
    }

    @PostMapping("/workflows/execute")
    public String executeWorkflow(@RequestBody Map<String, String> request) {
        return "Workflow executed: " + request.getOrDefault("workflowId", "unknown");
    }
}
