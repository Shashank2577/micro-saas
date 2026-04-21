package com.microsaas.dealbrain.service;

import com.microsaas.dealbrain.model.DealRecommendation;
import com.microsaas.dealbrain.model.DealRiskSignal;
import com.microsaas.dealbrain.repository.DealRecommendationRepository;
import com.microsaas.dealbrain.repository.DealRiskSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NextActionRecommendationService {

    private final DealRiskSignalRepository riskSignalRepository;
    private final DealRecommendationRepository recommendationRepository;

    public List<DealRecommendation> generateRecommendations(UUID tenantId, UUID dealId) {
        List<DealRiskSignal> risks = riskSignalRepository.findByTenantIdAndDealId(tenantId, dealId);

        for (DealRiskSignal risk : risks) {
            if ("STALE_ACTIVITY".equals(risk.getSignalType())) {
                createRecommendation(tenantId, dealId, "Schedule follow-up call", "Deal has been inactive for 7 days.");
            } else if ("NO_ENGAGED_DECISION_MAKER".equals(risk.getSignalType())) {
                createRecommendation(tenantId, dealId, "Email Decision Maker", "Need to increase engagement with decision maker.");
            }
        }

        // Add a default recommendation if none exist
        List<DealRecommendation> existing = recommendationRepository.findByTenantIdAndDealId(tenantId, dealId);
        if (existing.isEmpty()) {
            createRecommendation(tenantId, dealId, "Send check-in email", "Maintain regular cadence.");
        }

        return recommendationRepository.findByTenantIdAndDealId(tenantId, dealId);
    }

    private void createRecommendation(UUID tenantId, UUID dealId, String action, String reason) {
        List<DealRecommendation> existing = recommendationRepository.findByTenantIdAndDealId(tenantId, dealId)
            .stream().filter(r -> action.equals(r.getAction())).collect(Collectors.toList());

        if (existing.isEmpty()) {
            DealRecommendation rec = new DealRecommendation();
            rec.setTenantId(tenantId);
            rec.setDealId(dealId);
            rec.setAction(action);
            rec.setReason(reason);
            rec.setStatus("PENDING");
            recommendationRepository.save(rec);
        }
    }
}
