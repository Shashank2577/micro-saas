package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealActivity;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.model.Stakeholder;
import com.microsaas.dealbrain.repository.DealActivityRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import com.microsaas.dealbrain.repository.StakeholderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiskSignalDetectionService {

    private final DealActivityRepository activityRepository;
    private final StakeholderRepository stakeholderRepository;
    private final DealRiskSignalRepository riskSignalRepository;

    public List<DealRiskSignal> detectRisks(UUID tenantId, UUID dealId) {
        List<DealActivity> activities = activityRepository.findByTenantIdAndDealId(tenantId, dealId);
        List<Stakeholder> stakeholders = stakeholderRepository.findByTenantIdAndDealId(tenantId, dealId);

        LocalDateTime now = LocalDateTime.now();

        // Check for stale activity
        boolean hasRecentActivity = activities.stream()
                .anyMatch(a -> a.getTimestamp() != null && a.getTimestamp().isAfter(now.minusDays(7)));

        if (!hasRecentActivity) {
            createOrUpdateSignal(tenantId, dealId, "STALE_ACTIVITY", "HIGH", "No activity in the last 7 days.");
        }

        // Check for lack of decision maker engagement
        boolean hasEngagedDecisionMaker = stakeholders.stream()
                .anyMatch(s -> "DECISION_MAKER".equalsIgnoreCase(s.getRole()) &&
                              ("HIGH".equalsIgnoreCase(s.getEngagementLevel()) || "MEDIUM".equalsIgnoreCase(s.getEngagementLevel())));

        if (!hasEngagedDecisionMaker) {
            createOrUpdateSignal(tenantId, dealId, "NO_ENGAGED_DECISION_MAKER", "HIGH", "Decision maker is not engaged.");
        }

        return riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId);
    }

    private void createOrUpdateSignal(UUID tenantId, UUID dealId, String type, String severity, String description) {
        List<DealRiskSignal> existing = riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId)
            .stream().filter(s -> type.equals(s.getSignalType())).collect(Collectors.toList());

        if (existing.isEmpty()) {
            DealRiskSignal signal = new DealRiskSignal();
            signal.setTenantId(tenantId);
            signal.setDealId(dealId);
            signal.setSignalType(type);
            signal.setSeverity(severity);
            signal.setDescription(description);
            riskSignalRepository.save(signal);
        }
    }
}
