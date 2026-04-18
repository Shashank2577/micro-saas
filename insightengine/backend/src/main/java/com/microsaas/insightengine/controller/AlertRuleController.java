package com.microsaas.insightengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.insightengine.entity.AlertRule;
import com.microsaas.insightengine.repository.AlertRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts/rules")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleRepository alertRuleRepository;

    @GetMapping
    public List<AlertRule> getRules() {
        UUID tenantId = TenantContext.require();
        return alertRuleRepository.findByTenantId(tenantId);
    }

    @PostMapping
    public AlertRule createRule(@RequestBody AlertRule rule) {
        UUID tenantId = TenantContext.require();
        rule.setTenantId(tenantId);
        return alertRuleRepository.save(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertRule> updateRule(@PathVariable UUID id, @RequestBody AlertRule updatedRule) {
        UUID tenantId = TenantContext.require();
        return alertRuleRepository.findByIdAndTenantId(id, tenantId)
                .map(rule -> {
                    rule.setName(updatedRule.getName());
                    rule.setMetricName(updatedRule.getMetricName());
                    rule.setConditionType(updatedRule.getConditionType());
                    rule.setThreshold(updatedRule.getThreshold());
                    rule.setSlackChannel(updatedRule.getSlackChannel());
                    rule.setIsActive(updatedRule.getIsActive());
                    return ResponseEntity.ok(alertRuleRepository.save(rule));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        if (alertRuleRepository.findByIdAndTenantId(id, tenantId).isPresent()) {
            alertRuleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
