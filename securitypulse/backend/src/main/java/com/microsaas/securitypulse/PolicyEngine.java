package com.microsaas.securitypulse;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PolicyEngine {

    public String evaluate(List<Finding> findings, List<Policy> policies) {
        for (Finding finding : findings) {
            for (Policy policy : policies) {
                if (finding.getSeverity().equalsIgnoreCase(policy.getRule())) {
                    if ("BLOCK".equalsIgnoreCase(policy.getAction())) {
                        return "BLOCK";
                    }
                }
                if ("BLOCK_SECRETS".equalsIgnoreCase(policy.getRule()) && "TRUFFLEHOG".equalsIgnoreCase(finding.getTool())) {
                    return "BLOCK";
                }
            }
        }
        return "ALLOW";
    }
}
