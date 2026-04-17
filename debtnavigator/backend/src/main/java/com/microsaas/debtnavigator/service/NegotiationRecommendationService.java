package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.dto.NegotiationRecommendationDto;
import com.microsaas.debtnavigator.entity.Debt;
import com.microsaas.debtnavigator.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NegotiationRecommendationService {

    private final DebtRepository debtRepository;

    public List<NegotiationRecommendationDto> getRecommendations(String tenantId) {
        List<Debt> debts = debtRepository.findByTenantId(tenantId);
        List<NegotiationRecommendationDto> recommendations = new ArrayList<>();

        for (Debt debt : debts) {
            if ("CREDIT_CARD".equalsIgnoreCase(debt.getType()) && debt.getApr().compareTo(BigDecimal.valueOf(18.0)) > 0) {
                String script = String.format("Hello, I've been a loyal customer for years. I noticed my APR is currently at %.2f%%, which is higher than average. " +
                        "I have received balance transfer offers from other cards, but I would prefer to stay with you. " +
                        "Can you lower my APR to help me pay down my balance faster?", debt.getApr());

                int likelihood = debt.getApr().compareTo(BigDecimal.valueOf(25.0)) > 0 ? 80 : 40;

                recommendations.add(NegotiationRecommendationDto.builder()
                        .debtId(debt.getId())
                        .debtName(debt.getName())
                        .recommendedScript(script)
                        .likelihoodOfSuccessPercentage(likelihood)
                        .build());
            }
        }
        return recommendations;
    }
}
