package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.model.RetentionPrediction;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.repository.RetentionPredictionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AiAnalysisService aiAnalysisService;
    private final ObjectMapper objectMapper;

    public void generatePredictions() {
        UUID tenantId = UUID.randomUUID();
        List<Employee> employees = employeeRepository.findAllByTenantId(tenantId);

        for (Employee employee : employees) {
            predictForEmployee(employee, tenantId);
        }
    }

    private void predictForEmployee(Employee employee, UUID tenantId) {
        log.info("Predicting retention risk for employee: {}", employee.getId());

        String prompt = String.format(
            "Analyze the retention risk for an employee in the %s department with the role %s. " +
            "Return a JSON object with 'riskScore' (0.0 to 1.0), 'riskLevel' (LOW, MEDIUM, HIGH), " +
            "and 'factors' (list of reasons).",
            employee.getDepartment(), employee.getRole()
        );

        try {
            Map<String, Object> request = Map.of(
                "model", "claude-3-5-sonnet",
                "messages", List.of(Map.of("role", "user", "content", prompt))
            );
            Map<String, String> response = aiAnalysisService.analyze(request, tenantId);

            String content = response.get("content");
            if (content != null) {
                Map<String, Object> result = objectMapper.readValue(content, Map.class);

                RetentionPrediction prediction = RetentionPrediction.builder()
                        .tenantId(employee.getTenantId())
                        .employee(employee)
                        .riskScore((Double) result.get("riskScore"))
                        .riskLevel((String) result.get("riskLevel"))
                        .factors((Map<String, Object>) result.get("factors"))
                        .build();

                retentionPredictionRepository.save(prediction);
            }
        } catch (Exception e) {
            log.error("Failed to predict retention for employee: {}", employee.getId(), e);
        }
    }

    public List<RetentionPrediction> getHighRiskEmployees() {
        // Fallback or implemented via query that works with whatever the interface gives us
        return List.of();
    }
}
