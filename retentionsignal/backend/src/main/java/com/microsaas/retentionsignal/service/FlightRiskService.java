package com.microsaas.retentionsignal.service;

import com.microsaas.retentionsignal.model.FlightRiskScore;
import com.microsaas.retentionsignal.model.RetentionSignal;
import com.microsaas.retentionsignal.repository.FlightRiskScoreRepository;
import com.microsaas.retentionsignal.repository.RetentionSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightRiskService {

    private final RetentionSignalRepository retentionSignalRepository;
    private final FlightRiskScoreRepository flightRiskScoreRepository;

    @Transactional
    public FlightRiskScore calculateRisk(UUID employeeId, UUID tenantId) {
        List<RetentionSignal> signals = retentionSignalRepository.findByEmployeeIdAndTenantId(employeeId, tenantId);

        int totalScore = 0;
        for (RetentionSignal signal : signals) {
            totalScore += signal.getSignalType().getWeight();
        }

        if (totalScore > 100) {
            totalScore = 100;
        }

        String topRiskFactors = signals.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getSignalType().getWeight(), s1.getSignalType().getWeight()))
                .limit(3)
                .map(s -> s.getSignalType().name())
                .collect(Collectors.joining(","));

        FlightRiskScore score = new FlightRiskScore();
        score.setEmployeeId(employeeId);
        score.setScore(totalScore);
        score.setTopRiskFactors(topRiskFactors);
        score.setCalculatedAt(LocalDateTime.now());
        score.setTenantId(tenantId);

        return flightRiskScoreRepository.save(score);
    }

    public List<FlightRiskScore> getHighRisk(UUID tenantId) {
        return flightRiskScoreRepository.findByTenantIdAndScoreGreaterThanEqual(tenantId, 70);
    }
}
