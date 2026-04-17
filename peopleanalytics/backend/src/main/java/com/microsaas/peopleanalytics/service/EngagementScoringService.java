package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.EngagementScore;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.EngagementScoreRepository;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.client.LiteLLMClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EngagementScoringService {

    private final EngagementScoreRepository scoreRepository;
    private final EmployeeRepository employeeRepository;
    private final LiteLLMClient llmClient;

    public List<EngagementScore> getTrends(UUID tenantId) {
        return scoreRepository.findByTenantId(tenantId);
    }

    @Transactional
    public void calculateScores(UUID tenantId) {
        List<Employee> employees = employeeRepository.findByTenantId(tenantId);
        for (Employee emp : employees) {
            String prompt = "Calculate an engagement score (0-100) for employee " + emp.getFirstName() + " based on hypothetical recent activity.";
            String aiResponse = llmClient.getInsights(prompt);

            // Simplified parsing - in a real app we'd ask for JSON from LLM
            BigDecimal score = new BigDecimal("85.0"); // Defaulting
            if (aiResponse.contains("score:")) {
                // mock parse
            }

            Map<String, Object> factors = new HashMap<>();
            factors.put("ai_insight", aiResponse);
            factors.put("last_calculated", LocalDateTime.now().toString());

            EngagementScore engScore = new EngagementScore();
            engScore.setTenantId(tenantId);
            engScore.setEmployee(emp);
            engScore.setScore(score);
            engScore.setCalculatedAt(LocalDateTime.now());
            engScore.setFactors(factors);

            scoreRepository.save(engScore);
        }
    }
}
