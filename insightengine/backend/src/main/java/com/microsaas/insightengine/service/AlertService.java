package com.microsaas.insightengine.service;

import com.microsaas.insightengine.entity.AlertRule;
import com.microsaas.insightengine.entity.Insight;
import com.microsaas.insightengine.repository.AlertRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRuleRepository alertRuleRepository;

    public void evaluateAlertRules(Insight insight) {
        List<AlertRule> rules = alertRuleRepository.findByTenantIdAndIsActiveTrue(insight.getTenantId());
        
        for (AlertRule rule : rules) {
            boolean isTriggered = false;
            
            // Check condition
            if ("ANOMALY_SEVERITY".equals(rule.getConditionType()) && insight.getImpactScore() >= rule.getThreshold()) {
                isTriggered = true;
            } else if ("TREND_ACCELERATION".equals(rule.getConditionType()) && "TREND".equals(insight.getType()) && insight.getImpactScore() >= rule.getThreshold()) {
                isTriggered = true;
            }
            // Add more condition logic as needed
            
            if (isTriggered) {
                sendSlackNotification(rule.getSlackChannel(), insight);
            }
        }
    }

    public void sendSlackNotification(String channel, Insight insight) {
        // Mock slack notification
        log.info("SLACK NOTIFICATION to [{}]: Alert Triggered! Insight: {} (Impact: {})", 
                channel, insight.getTitle(), insight.getImpactScore());
    }
}
