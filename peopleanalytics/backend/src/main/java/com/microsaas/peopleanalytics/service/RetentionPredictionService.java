package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.RetentionPredictionRepository;
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
public class RetentionPredictionService {

    private final RetentionPredictionRepository retentionRepository;
    private final EmployeeRepository employeeRepository;
    private final LiteLLMClient llmClient;

    public List<RetentionPrediction> getRisks(UUID tenantId) {
        return retentionRepository.findByTenantId(tenantId);
    }

    @Transactional
    public void predictRisks(UUID tenantId) {
        List<Employee> employees = employeeRepository.findByTenantId(tenantId);
        for (Employee emp : employees) {
            String prompt = "Evaluate flight risk (0-1 probability) for employee " + emp.getFirstName() + " given standard industry turnover data.";
            String aiResponse = llmClient.getInsights(prompt);

            BigDecimal riskScore = new BigDecimal("0.15"); // Mock probability
            String riskLevel = "LOW";

            Map<String, Object> factors = new HashMap<>();
            factors.put("reasoning", aiResponse);

            RetentionPrediction prediction = new RetentionPrediction();
            prediction.setTenantId(tenantId);
            prediction.setEmployee(emp);
            prediction.setRiskScore(riskScore);
            prediction.setRiskLevel(riskLevel);
            prediction.setFactors(factors);
            prediction.setPredictedAt(LocalDateTime.now());

            retentionRepository.save(prediction);
        }
    }
}
