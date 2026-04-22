package com.microsaas.compensationos.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.PayEquityAnalysisResponse;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayEquityService {

    private final EmployeeCompensationRepository employeeCompensationRepository;
    private final AiService aiService;

    @Value("${cc.ai.default-model:claude-sonnet-4-6}")
    private String defaultModel;

    public PayEquityAnalysisResponse analyzePayEquity(String role) {
        UUID tenantId = TenantContext.require();
        List<EmployeeCompensation> employees;
        if (role != null) {
            employees = employeeCompensationRepository.findByTenantIdAndRole(tenantId, role);
        } else {
            employees = employeeCompensationRepository.findByTenantId(tenantId);
        }

        Map<String, BigDecimal> avgByGender = calculateAverageByField(employees, EmployeeCompensation::getGender);
        Map<String, BigDecimal> avgByEthnicity = calculateAverageByField(employees, EmployeeCompensation::getEthnicity);

        PayEquityAnalysisResponse response = new PayEquityAnalysisResponse();
        response.setRole(role != null ? role : "All Roles");
        response.setAvgSalaryByGender(avgByGender);
        response.setAvgSalaryByEthnicity(avgByEthnicity);

        String prompt = "Analyze the following pay equity data for " + (role != null ? role : "all roles") + ". " +
                "Gender averages: " + avgByGender + ". Ethnicity averages: " + avgByEthnicity + ". " +
                "Provide a brief compliance report and insight on potential pay gaps.";
        
        try {
            String insight = aiService.chat(new ChatRequest(defaultModel, List.of(new ChatMessage("user", prompt)), null, null)).content();
            response.setAiInsight(insight);
        } catch (Exception e) {
            response.setAiInsight("AI analysis currently unavailable.");
        }

        return response;
    }

    private Map<String, BigDecimal> calculateAverageByField(List<EmployeeCompensation> employees, 
                                                           java.util.function.Function<EmployeeCompensation, String> fieldExtractor) {
        Map<String, List<EmployeeCompensation>> grouped = employees.stream()
                .filter(e -> fieldExtractor.apply(e) != null && !fieldExtractor.apply(e).isEmpty())
                .collect(Collectors.groupingBy(fieldExtractor));

        Map<String, BigDecimal> averages = new HashMap<>();
        grouped.forEach((key, list) -> {
            BigDecimal sum = list.stream().map(EmployeeCompensation::getBaseSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avg = sum.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);
            averages.put(key, avg);
        });
        return averages;
    }
}
