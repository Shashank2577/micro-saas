package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.repository.RetentionPredictionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetentionPredictionService {
    private final RetentionPredictionRepository retentionPredictionRepository;
    private final EmployeeRepository employeeRepository;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    @Value("${cc.ai.default-model:claude-3-5-sonnet}")
    private String aiModel;

    @CircuitBreaker(name = "aiService")
    @Retry(name = "aiService")
    public void generatePredictions() {
        UUID tenantId = TenantContext.getTenantId();
        List<Employee> employees = employeeRepository.findAllByTenantId(tenantId);

        for (Employee employee : employees) {
            predictForEmployee(employee);
        }
    }

    private void predictForEmployee(Employee employee) {
        log.info("Predicting retention risk for employee: {}", employee.getId());

        String prompt = String.format(
            "Analyze the retention risk for an employee in the %s department with the role %s. " +
            "Return a JSON object with 'riskScore' (0.0 to 1.0), 'riskLevel' (LOW, MEDIUM, HIGH), " +
            "and 'factors' (list of reasons).",
            employee.getDepartment(), employee.getRole()
        );

        try {
            ChatResponse response = aiService.chat(new ChatRequest(
                aiModel,
                List.of(new ChatMessage("user", prompt)),
                0.0,
                500
            ));

            Map<String, Object> result = objectMapper.readValue(response.content(), Map.class);

            RetentionPrediction prediction = RetentionPrediction.builder()
                    .tenantId(employee.getTenantId())
                    .employee(employee)
                    .riskScore((Double) result.get("riskScore"))
                    .riskLevel((String) result.get("riskLevel"))
                    .factors((Map<String, Object>) result.get("factors"))
                    .build();

            retentionPredictionRepository.save(prediction);
        } catch (Exception e) {
            log.error("Failed to predict retention for employee: {}", employee.getId(), e);
        }
    }

    public List<RetentionPrediction> getHighRiskEmployees() {
        return retentionPredictionRepository.findAllByTenantIdAndRiskLevel(TenantContext.getTenantId(), "HIGH");
    }
}
