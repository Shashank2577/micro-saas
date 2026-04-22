package com.microsaas.dependencyradar.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.microsaas.dependencyradar.model.UpgradeImpactAnalysis;
import com.microsaas.dependencyradar.repository.UpgradeImpactAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiImpactService {
    private final AiService aiService;
    private final UpgradeImpactAnalysisRepository analysisRepository;

    public UpgradeImpactAnalysis analyzeImpact(UUID dependencyId, String targetVersion, String currentVersion, String ecosystem) {
        String tenantId = "system";

        String prompt = String.format("Analyze the impact of upgrading %s dependency from %s to %s.", ecosystem, currentVersion, targetVersion);
        ChatRequest req = new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), 0.7, 1000);
        String analysis = aiService.chat(req).content();

        UpgradeImpactAnalysis result = new UpgradeImpactAnalysis();
        result.setTenantId(tenantId);
        result.setDependencyId(dependencyId);
        result.setTargetVersion(targetVersion);
        result.setAnalysis(analysis);

        return analysisRepository.save(result);
    }
}
