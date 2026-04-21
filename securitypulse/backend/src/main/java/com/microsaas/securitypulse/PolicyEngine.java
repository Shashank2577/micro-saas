package com.microsaas.securitypulse;

import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyEngine {

    private final PolicyDecisionRepository policyDecisionRepository;
    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public PolicyEngine(PolicyDecisionRepository policyDecisionRepository,
                        WebhookService webhookService,
                        ObjectMapper objectMapper) {
        this.policyDecisionRepository = policyDecisionRepository;
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PolicyDecision evaluate(UUID scanJobId, List<Finding> findings, List<Policy> policies, UUID tenantId) {
        String decision = "ALLOW";
        String reason = "No policy violations found.";

        for (Finding finding : findings) {
            for (Policy policy : policies) {
                if (finding.getSeverity().equalsIgnoreCase(policy.getRule())) {
                    if ("BLOCK".equalsIgnoreCase(policy.getAction())) {
                        decision = "BLOCK";
                        reason = "Policy '" + policy.getName() + "' violated by finding with severity " + finding.getSeverity();
                        break;
                    }
                }
                if ("BLOCK_SECRETS".equalsIgnoreCase(policy.getRule()) && "TRUFFLEHOG".equalsIgnoreCase(finding.getTool())) {
                    decision = "BLOCK";
                    reason = "Policy '" + policy.getName() + "' violated: Secrets detected by TruffleHog";
                    break;
                }
            }
            if ("BLOCK".equals(decision)) break;
        }

        PolicyDecision policyDecision = PolicyDecision.builder()
                .id(UUID.randomUUID())
                .scanJobId(scanJobId)
                .decision(decision)
                .reason(reason)
                .tenantId(tenantId)
                .build();

        policyDecisionRepository.save(policyDecision);

        if ("BLOCK".equals(decision)) {
            emitPolicyViolation(policyDecision);
        }

        return policyDecision;
    }

    private void emitPolicyViolation(PolicyDecision decision) {
        try {
            webhookService.dispatch(decision.getTenantId(), "policy-violation",
                    objectMapper.writeValueAsString(decision));
        } catch (JsonProcessingException e) {
            // Log error
        }
    }
}
