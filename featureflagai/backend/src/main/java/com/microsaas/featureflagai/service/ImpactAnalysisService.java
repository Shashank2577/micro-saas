package com.microsaas.featureflagai.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImpactAnalysisService {

    private final FeatureFlagRepository flagRepository;
    private final AiService aiService;

    public String analyzeImpact(UUID flagId, String metricsData) {
        UUID tenantId = TenantContext.require();
        Optional<FeatureFlag> flagOpt = flagRepository.findByIdAndTenantId(flagId, tenantId);

        if (flagOpt.isEmpty()) {
            return "Flag not found";
        }

        String prompt = "Analyze the conversion impact of feature flag '" + flagOpt.get().getName() +
                        "' based on the following metrics data: " + metricsData +
                        ". Provide the conversion delta and summarize if the flag caused positive or negative effects.";

        ChatRequest request = new ChatRequest(
            "claude-3-5-sonnet-20240620",
            List.of(new ChatMessage("user", prompt)),
            null,
            null
        );

        return aiService.chat(request).content();
    }
}
