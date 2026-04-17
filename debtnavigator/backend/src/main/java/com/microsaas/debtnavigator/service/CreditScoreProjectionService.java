package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.dto.CreditScoreProjectionDto;
import com.microsaas.debtnavigator.entity.CreditScoreHistory;
import com.microsaas.debtnavigator.repository.CreditScoreHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditScoreProjectionService {

    private final CreditScoreHistoryRepository creditScoreHistoryRepository;

    public CreditScoreProjectionDto projectScore(String tenantId, String strategyType, int monthsOut) {
        List<CreditScoreHistory> history = creditScoreHistoryRepository.findByTenantIdOrderByRecordedDateDesc(tenantId);
        int currentScore = history.isEmpty() ? 650 : history.get(0).getScore();

        int pointIncreasePerMonth = "AVALANCHE".equalsIgnoreCase(strategyType) ? 2 : 1;
        int projectedScore = Math.min(850, currentScore + (pointIncreasePerMonth * monthsOut));

        String rationale = String.format("Paying off high interest debt via %s strategy improves credit utilization ratio, projecting a score increase of %d points over %d months.",
                strategyType, projectedScore - currentScore, monthsOut);

        return CreditScoreProjectionDto.builder()
                .currentScore(currentScore)
                .projectedScore(projectedScore)
                .monthsOut(monthsOut)
                .rationale(rationale)
                .build();
    }
}
