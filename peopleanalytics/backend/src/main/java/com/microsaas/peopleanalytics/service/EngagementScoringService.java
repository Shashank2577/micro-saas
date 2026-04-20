package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.peopleanalytics.model.EngagementScore;
import com.microsaas.peopleanalytics.model.SurveyResponse;
import com.microsaas.peopleanalytics.repository.EngagementScoreRepository;
import com.microsaas.peopleanalytics.repository.SurveyResponseRepository;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngagementScoringService {
    private final EngagementScoreRepository engagementScoreRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 0 0 * * SUN") // Every Sunday at midnight
    @Transactional
    public void calculateWeeklyScores() {
        log.info("Calculating weekly engagement scores");
    }

    @Transactional
    public void calculateScoresForTenant(UUID tenantId) {
        TenantContext.setTenantId(tenantId);
        try {
            List<SurveyResponse> responses = surveyResponseRepository.findAllByTenantId(tenantId);

            Map<UUID, List<SurveyResponse>> employeeResponses = responses.stream()
                    .collect(Collectors.groupingBy(r -> r.getEmployee().getId()));

            employeeResponses.forEach((employeeId, resps) -> {
                double avgScore = resps.stream()
                        .mapToInt(SurveyResponse::getScore)
                        .average()
                        .orElse(0.0) * 10; // Scale 1-10 to 0-100

                EngagementScore score = EngagementScore.builder()
                        .tenantId(tenantId)
                        .employee(employeeRepository.findById(employeeId).orElseThrow())
                        .score(avgScore)
                        .source("SURVEY")
                        .build();

                engagementScoreRepository.save(score);
            });
        } finally {
            TenantContext.clear();
        }
    }

    public List<EngagementScore> getScores() {
        return engagementScoreRepository.findAllByTenantId(TenantContext.getTenantId());
    }
}
