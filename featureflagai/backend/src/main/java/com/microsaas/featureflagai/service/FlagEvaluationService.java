package com.microsaas.featureflagai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.featureflagai.domain.FeatureFlag;
import com.microsaas.featureflagai.domain.FlagEvaluation;
import com.microsaas.featureflagai.repository.FeatureFlagRepository;
import com.microsaas.featureflagai.repository.FlagEvaluationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlagEvaluationService {

    private final FeatureFlagRepository flagRepository;
    private final FlagEvaluationRepository evaluationRepository;
    private final QueueService queueService;
    private final ObjectMapper objectMapper;

    public boolean evaluate(UUID flagId, String userId, JsonNode context) {
        UUID tenantId = TenantContext.require();
        Optional<FeatureFlag> flagOpt = flagRepository.findByIdAndTenantId(flagId, tenantId);

        if (flagOpt.isEmpty()) {
            log.warn("Flag not found for evaluation: {}", flagId);
            return false;
        }

        FeatureFlag flag = flagOpt.get();
        if (!flag.isEnabled()) {
            return false;
        }

        // Basic hashing logic for rollout percentage
        boolean result = false;
        if (flag.getRolloutPct() == 100) {
            result = true;
        } else if (flag.getRolloutPct() > 0) {
            int bucket = getHashBucket(userId + flagId.toString());
            result = bucket < flag.getRolloutPct();
        }

        // Record evaluation asynchronously/event
        try {
            FlagEvaluation eval = FlagEvaluation.builder()
                    .tenantId(tenantId)
                    .flagId(flagId)
                    .userId(userId)
                    .result(result)
                    .context(context)
                    .build();
            evaluationRepository.save(eval);

            String eventPayload = objectMapper.writeValueAsString(eval);
            queueService.enqueue("featureflagai.flag.evaluated", eventPayload, 0);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize evaluation event", e);
        }

        return result;
    }

    private int getHashBucket(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            int hash = Math.abs(java.nio.ByteBuffer.wrap(digest).getInt());
            return hash % 100;
        } catch (NoSuchAlgorithmException e) {
            return 0; // fallback
        }
    }
}
